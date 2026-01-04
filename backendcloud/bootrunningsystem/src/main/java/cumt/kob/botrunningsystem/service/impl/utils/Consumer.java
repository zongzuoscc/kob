package cumt.kob.botrunningsystem.service.impl.utils;

import cumt.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread {

    private Bot bot;
    private static RestTemplate restTemplate;
    private static final String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout,Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt();//中断当前线程 ，最多等待timeout毫秒
        }
    }

    private String addUid(String code,String uid){//在code中的bot类名后添加uid，如果是实现docker写法则无需进行此操作
        int k=code.indexOf(" implements cumt.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k)+uid+code.substring(k);
    }

    @Override
    public void run() {

        //为了保证每次都能重新编译代码需要在类名后面加一个uuid
        UUID uuid=UUID.randomUUID();
        String uid=uuid.toString().substring(0,8);

        BotInterface botInterface= Reflect.compile(
            "cumt.kob.botrunningsystem.utils.Bot"+uid,
            addUid(bot.getBotCode(),uid)
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());

        System.out.println("move-direction:"+bot.getUserId()+" "+direction);

        MultiValueMap<String, String> data=new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);
    }
}
