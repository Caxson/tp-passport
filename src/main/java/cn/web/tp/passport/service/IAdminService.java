package cn.web.tp.passport.service;

import cn.web.tp.passport.pojo.dto.AdminAddNewDTO;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.vo.AdminListItemVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IAdminService {

    @Transactional
    void addNew(AdminAddNewDTO adminAddNewDTO);

    String login(AdminLoginDTO adminLoginDTO, HttpServletResponse response);

    List<AdminListItemVO> list();
}
