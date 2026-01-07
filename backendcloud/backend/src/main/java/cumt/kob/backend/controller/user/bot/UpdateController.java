package cumt.kob.backend.controller.user.bot;

import cumt.kob.backend.mapper.BotMapper;
import cumt.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @RequestMapping("/api/user/bot/update")
    public Map<String,String> update(@RequestParam Map<String,String> data){
        return updateService.update(data);
    }
}
