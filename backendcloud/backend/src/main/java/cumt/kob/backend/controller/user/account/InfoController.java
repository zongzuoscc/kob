package cumt.kob.backend.controller.user.account;

import cumt.kob.backend.service.user.account.InfoService;
import cumt.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
public class InfoController {

    @Autowired
    private InfoService infoService;

    @RequestMapping("/api/user/account/info")
    public Map<String,String> getInfo(){
        return infoService.getInfo();
    }
}
