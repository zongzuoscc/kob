package cumt.kob.matchingsystem.service.impl;

import cumt.kob.matchingsystem.service.MatchingService;
import cumt.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    //全局只有一个线程所以可以定义成一个静态的变量
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("addPlayer:" + userId + "，rating:" + rating);
        matchingPool.addPlayer(userId, rating,botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("removePlayer:" + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
