package cumt.kob.backend.service.impl.pk;

import cumt.kob.backend.consumer.WebSocketServer;
import cumt.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        System.out.println("start game" + aId + " " + bId);
        WebSocketServer.startGame(aId,bId);
        return "start game success";
    }
}
