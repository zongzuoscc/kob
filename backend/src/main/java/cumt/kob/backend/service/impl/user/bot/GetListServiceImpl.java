package cumt.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cumt.kob.backend.mapper.BotMapper;
import cumt.kob.backend.pojo.Bot;
import cumt.kob.backend.pojo.User;
import cumt.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cumt.kob.backend.service.user.bot.GetListService;

import java.util.List;

@Service
public class GetListServiceImpl implements GetListService {

        @Autowired
        private BotMapper botMapper;

    @Override
    public List<Bot> getList() {

        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        List<Bot> botList = botMapper.selectList(new QueryWrapper<Bot>().eq("user_id", user.getId()));

        return botList;
    }
}
