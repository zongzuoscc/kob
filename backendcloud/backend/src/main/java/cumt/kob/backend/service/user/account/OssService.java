package cumt.kob.backend.service.user.account;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface OssService {
    Map<String, String> uploadPhoto(MultipartFile file);
}