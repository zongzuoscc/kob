package cumt.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot(Integer userId,String botCode,String input){
        lock.lock();//涉及到对队列的修改，上锁
        try {
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot) {//目前该项目只支持用java编写的机器人，后续如果为了保证安全性以及兼容其他语言只需要修改这个方法即可
        //可以用docker来运行，但是目前项目只支持java语言，所以这里就不考虑docker了
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);

    }

    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await();//默认包含锁释放的操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else {
                Bot bot = bots.remove();//返回并删除队头元素
                lock.unlock();
                consume(bot);//这个操作比较耗时所以在解锁操作之后执行,这一系列操作本质上就是手动实现了一个操作队列
            }
        }
    }
}
