package cn.web.tp.passport.service;

import cn.web.tp.passport.pojo.dto.AdminAddNewDTO;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.entity.Admin;
import cn.web.tp.passport.pojo.entity.AdminLog;
import cn.web.tp.passport.pojo.vo.AdminListItemVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IAdminService {
    /**
     * 写入日志
     * @param adminLog
     */
    void insertLog(AdminLog adminLog);

    /**
     * 添加管理员
     * @param adminAddNewDTO
     */
    @Transactional
    void addNew(AdminAddNewDTO adminAddNewDTO);

    /**
     * 管理员登录
     * @param adminLoginDTO
     * @param response
     * @return
     */
    String login(AdminLoginDTO adminLoginDTO, HttpServletResponse response);

    /**
     * 管理员列表
     * @return
     */
    List<AdminListItemVO> list();

    /**
     * 获取主体类信息
     * @return
     */
    Admin getPrinciple();
}
