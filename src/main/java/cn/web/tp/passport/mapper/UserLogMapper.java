package cn.web.tp.passport.mapper;

import cn.web.tp.passport.pojo.entity.AdminLog;
import cn.web.tp.passport.pojo.entity.UserLog;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogMapper {
    /**
     * 写入日志
     * @param userLog
     * @return
     */
    int insert(UserLog userLog);
}
