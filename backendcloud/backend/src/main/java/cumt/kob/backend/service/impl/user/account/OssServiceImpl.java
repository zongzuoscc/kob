package cumt.kob.backend.service.impl.user.account;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import cumt.kob.backend.service.user.account.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Override
    public Map<String, String> uploadPhoto(MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        if (file == null || file.isEmpty()) {
            map.put("error_message", "文件不能为空");
            return map;
        }

        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) originalFilename = ".jpg";
            // 获取后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成唯一文件名
            String objectName = "user-photos/" + UUID.randomUUID().toString() + suffix;

            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build("https://" + endpoint, accessKeyId, accessKeySecret);

            // 上传
            ossClient.putObject(bucketName, objectName, file.getInputStream());

            // 关闭OSSClient
            ossClient.shutdown();

            // 返回URL
            String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
            map.put("error_message", "success");
            map.put("url", url);

        } catch (IOException e) {
            e.printStackTrace();
            map.put("error_message", "上传失败");
        }

        return map;
    }
}