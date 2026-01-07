package cumt.kob.backend.controller.user.account;

import cumt.kob.backend.service.user.account.UpdateUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateUserInfoController {
    @Autowired
    private UpdateUserInfoService updateUserInfoService;

    @PostMapping("/api/user/account/update")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        return updateUserInfoService.update(data);
    }
}