package cn.web.tp.passport.service;

import cn.web.tp.passport.pojo.dto.AdminAddNewDTO;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.dto.UserAddNewDTO;
import cn.web.tp.passport.pojo.dto.UserLoginDTO;
import cn.web.tp.passport.pojo.entity.AdminLog;
import cn.web.tp.passport.pojo.entity.User;
import cn.web.tp.passport.pojo.entity.UserLog;
import cn.web.tp.passport.pojo.vo.AdminListItemVO;
import cn.web.tp.passport.pojo.vo.UserListItemVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {
    /**
     * 写入日志
     * @param userLog
     */
    void insertLog(UserLog userLog);

    /**
     * 用户注册功能
     * @param userAddNewDTO
     */
    @Transactional
    void addNew(UserAddNewDTO userAddNewDTO);

    /**
     * 用户登录
     * @param userLoginDTO
     * @param response
     * @return
     */
    String login(UserLoginDTO userLoginDTO, HttpServletResponse response);

    /**
     * 用户列表
     * @return
     */
    List<UserListItemVO> list();

    /**
     * 获取主体类信息
     * @return
     */
    User getPrinciple();
}
