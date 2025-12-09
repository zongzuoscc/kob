package cumt.kob.backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cumt.kob.backend.pojo.Bot;
import cumt.kob.backend.service.user.account.LoginService;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BotMapper extends BaseMapper<Bot> {

}
