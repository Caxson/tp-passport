package cn.web.tp.passport.service.impl;


import cn.web.tp.passport.exception.ServiceException;
import cn.web.tp.passport.mapper.AdminMapper;
import cn.web.tp.passport.mapper.AdminRoleMapper;
import cn.web.tp.passport.pojo.dto.AdminAddNewDTO;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.entity.Admin;
import cn.web.tp.passport.pojo.entity.AdminRole;
import cn.web.tp.passport.pojo.vo.AdminListItemVO;
import cn.web.tp.passport.security.AdminDetails;
import cn.web.tp.passport.security.SecurityCode;
import cn.web.tp.passport.service.IAdminService;
import cn.web.tp.passport.utils.CookieUtils;
import cn.web.tp.passport.utils.JwtUtils;
import cn.web.tp.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("执行管理员注册功能！");
        int row1 = adminMapper.countByUsername(adminAddNewDTO.getUsername());
        if (row1 > 0) throw new ServiceException(ServiceCode.ERR_CONFLICT, "用户名已存在！");

        Admin admin = new Admin();
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        int row2 = adminMapper.insert(admin);
        if (row2 != 1) throw new ServiceException(ServiceCode.ERR_UNKNOWN, "注册失败，请稍后重试！错误码1");

        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(admin.getId());
        List<Long> adminRoleIds = adminAddNewDTO.getRoleIds();
        log.debug("接收到的管理员角色id:{}",adminRoleIds);
        for(Long adminRoleId: adminRoleIds){
            adminRole.setRoleId(adminRoleId);
            int row3 = adminRoleMapper.insert(adminRole);
            if (row3 != 1) throw new ServiceException(ServiceCode.ERR_UNKNOWN, "注册失败，请稍后重试！错误码2");
        }
    }

    @Override
    public String login(AdminLoginDTO adminLoginDTO, HttpServletResponse response) {

        log.debug("执行管理员登录功能！");

        //Cookies
        CookieUtils.addCookies(adminLoginDTO.getRem(),adminLoginDTO.getUsername(),adminLoginDTO.getPassword(),response,60*24);

        //调用AuthenticationManager执行Spring Security的认证，与此同时框架自动调用loadUserByUsername方法
        Authentication  loginResult
                = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                adminLoginDTO.getUsername(), adminLoginDTO.getPassword()));

        log.debug("登陆成功!认证方法返回,{}>>>{}", loginResult, loginResult.getClass().getName());

        log.debug("尝试获取Principal:{},尝试获取User:{}", loginResult.getPrincipal(), loginResult.getPrincipal().getClass().getName());
        AdminDetails user = (AdminDetails) (loginResult.getPrincipal());

        Long id = user.getId();
        log.debug("登陆成功的用户ID:{}", id);

        String username = user.getUsername();
        log.debug("登录成功的用户名：{}", username);

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        log.debug("登陆成功的用户权限：{}", authorities);

        String authoritiesString = JSON.toJSONString(authorities);
        log.debug("将权限转换为JSON：{}", authoritiesString);

        int userIdentity = SecurityCode.ADMIN_ID;
        return JwtUtils.setJwt(id, username, authoritiesString, userIdentity,60*24*7);
    }

    @Override
    public List<AdminListItemVO> list() {
        log.debug("执行查询列表功能！");
        return adminMapper.list();
    }
}
