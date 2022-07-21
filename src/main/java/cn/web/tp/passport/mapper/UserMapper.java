package cn.web.tp.passport.mapper;

import cn.web.tp.passport.pojo.entity.User;
import cn.web.tp.passport.pojo.vo.UserListItemVO;
import cn.web.tp.passport.pojo.vo.UserLoginVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 注册
     * @param
     * @return
     */
    int insert(User user);

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
    UserLoginVO selectByUsername(String username);

    /**
     * 查询姓名列表
     * @return
     */
    List<UserListItemVO> list();
}
