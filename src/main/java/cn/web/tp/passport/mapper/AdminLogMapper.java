package cn.web.tp.passport.mapper;

import cn.web.tp.passport.pojo.entity.AdminLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLogMapper {
    /**
     * 写入日志
     * @param adminLog
     * @return
     */
    int insert(AdminLog adminLog);
}
