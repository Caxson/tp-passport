package cn.web.tp.passport.mapper;

import cn.web.tp.passport.pojo.entity.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMapper {
    /**
     *
     * @param
     * @return
     */
    int insert(UserRole userRole);
}
