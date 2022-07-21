package cn.web.tp.passport.service;

import cn.web.tp.passport.pojo.dto.AdminAddNewDTO;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.dto.UserAddNewDTO;
import cn.web.tp.passport.pojo.dto.UserLoginDTO;
import cn.web.tp.passport.pojo.vo.AdminListItemVO;
import cn.web.tp.passport.pojo.vo.UserListItemVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {

    @Transactional
    void addNew(UserAddNewDTO userAddNewDTO);

    String login(UserLoginDTO userLoginDTO, HttpServletResponse response);

    List<UserListItemVO> list();
}
