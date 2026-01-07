package cumt.kob.backend.service.impl.user.account;

import cumt.kob.backend.mapper.UserMapper;
import cumt.kob.backend.pojo.User;
import cumt.kob.backend.service.impl.utils.UserDetailsImpl;
import cumt.kob.backend.service.user.account.UpdateUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateUserInfoServiceImpl implements UpdateUserInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        String username = data.get("username");
        String photo = data.get("photo");
        String oldPassword = data.get("old_password"); // 获取旧密码
        String password = data.get("password");
        String confirmedPassword = data.get("confirmed_password");

        Map<String, String> map = new HashMap<>();

        // 1. 更新用户名
        if (username != null && !username.trim().isEmpty()) {
            username = username.trim();
            if (username.length() > 100) {
                map.put("error_message", "用户名长度不能超过100");
                return map;
            }
            user.setUsername(username);
        }

        // 2. 更新头像
        if (photo != null && !photo.trim().isEmpty()) {
            user.setPhoto(photo);
        }

        // 3. 更新密码 (增加了旧密码校验逻辑)
        if (password != null && !password.trim().isEmpty()) {
            // 必须验证旧密码
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                map.put("error_message", "修改密码必须输入原密码");
                return map;
            }

            // 验证旧密码是否正确
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                map.put("error_message", "原密码错误");
                return map;
            }

            if (confirmedPassword == null || confirmedPassword.trim().isEmpty()) {
                map.put("error_message", "请确认新密码");
                return map;
            }
            if (password.length() > 100) {
                map.put("error_message", "密码长度不能超过100");
                return map;
            }
            if (!password.equals(confirmedPassword)) {
                map.put("error_message", "两次输入的新密码不一致");
                return map;
            }
            user.setPassword(passwordEncoder.encode(password));
        }

        userMapper.updateById(user);
        map.put("error_message", "success");
        return map;
    }
}