package cn.web.tp.passport.mapper;

import cn.web.tp.passport.pojo.entity.Admin;
import cn.web.tp.passport.pojo.vo.AdminListItemVO;
import cn.web.tp.passport.pojo.vo.AdminLoginVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    /**
     * 注册
     * @param admin
     * @return
     */
    int insert(Admin admin);

    /**
     * 通过姓名查询数据
     * @param username
     * @return
     */
    int countByUsername(String username);


    /**
     * 登录
     * @param username
     * @return
     */
    AdminLoginVO selectByUsername(String username);

    /**
     * 查询姓名列表
     * @return
     */
    List<AdminListItemVO> list();
}
