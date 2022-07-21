package cn.web.tp.passport.mapper;

import cn.web.tp.passport.pojo.entity.AdminRole;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleMapper {
    /**
     *
     * @param adminRole
     * @return
     */
    int insert(AdminRole adminRole);
}
