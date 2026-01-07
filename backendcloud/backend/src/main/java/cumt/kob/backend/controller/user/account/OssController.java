package cumt.kob.backend.controller.user.account;

import cumt.kob.backend.service.user.account.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class OssController {
    @Autowired
    private OssService ossService;

    @PostMapping("/api/user/account/photo/upload")
    public Map<String, String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        return ossService.uploadPhoto(file);
    }
}