package cn.web.tp.passport.service.impl;


import cn.web.tp.passport.exception.ServiceException;
import cn.web.tp.passport.mapper.UserLogMapper;
import cn.web.tp.passport.mapper.UserMapper;
import cn.web.tp.passport.mapper.UserRoleMapper;
import cn.web.tp.passport.pojo.dto.UserAddNewDTO;
import cn.web.tp.passport.pojo.dto.UserLoginDTO;
import cn.web.tp.passport.pojo.entity.User;
import cn.web.tp.passport.pojo.entity.UserLog;
import cn.web.tp.passport.pojo.entity.UserRole;
import cn.web.tp.passport.pojo.vo.UserListItemVO;
import cn.web.tp.passport.pojo.vo.UserLoginVO;
import cn.web.tp.passport.security.LoginPrincipal;
import cn.web.tp.passport.security.SecurityCode;
import cn.web.tp.passport.security.ConsumerDetails;
import cn.web.tp.passport.service.IUserService;
import cn.web.tp.passport.utils.CookieUtils;
import cn.web.tp.passport.utils.JwtUtils;
import cn.web.tp.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserLogMapper userLogMapper;

    User userP = new User();

    @Async
    @Override
    public void insertLog(UserLog userLog) {
        String threadName = Thread.currentThread().getName();
        log.debug("insertLog.threadName={}",threadName);
        userLogMapper.insert(userLog);
    }

    @Override
    public void addNew(UserAddNewDTO userAddNewDTO) {
        log.debug("执行用户注册功能！");
        int row1 = userMapper.countByUsername(userAddNewDTO.getUsername());
        if (row1 > 0) throw new ServiceException(ServiceCode.ERR_CONFLICT, "用户名已存在！");

        User user = new User();
        BeanUtils.copyProperties(userAddNewDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int row2 = userMapper.insert(user);
        if (row2 != 1) throw new ServiceException(ServiceCode.ERR_UNKNOWN, "注册失败，请稍后重试！错误码1");

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        List<Long> userRoleIds = userAddNewDTO.getRoleIds();
        log.debug("接收到的用户角色id:{}", userRoleIds);
        for (Long adminRoleId : userRoleIds) {
            userRole.setRoleId(adminRoleId);
            int row3 = userRoleMapper.insert(userRole);
            if (row3 != 1) throw new ServiceException(ServiceCode.ERR_UNKNOWN, "注册失败，请稍后重试！错误码2");
        }
    }

    @Override
    public String login(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.debug("执行用户登录功能！");
        //Cookies
        CookieUtils.addCookies(userLoginDTO.getRem(), userLoginDTO.getUsername(), userLoginDTO.getPassword(), response, 60 * 24);

        LoginPrincipal loginPrincipal
                = new LoginPrincipal(
                userLoginDTO.getUsername(),
                SecurityCode.CONSUMER_ID);
        String loginIdentity = JSON.toJSONString(loginPrincipal);

        //调用AuthenticationManager执行Spring Security的认证，与此同时框架自动调用loadUserByUsername方法
        Authentication loginResult
                = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginIdentity, userLoginDTO.getPassword()));

        log.debug("登陆成功!认证方法返回,{}>>>{}", loginResult, loginResult.getClass().getName());

        log.debug("尝试获取Principal:{},尝试获取User:{}", loginResult.getPrincipal(), loginResult.getPrincipal().getClass().getName());
        ConsumerDetails user = (ConsumerDetails) (loginResult.getPrincipal());

        Long id = user.getId();
        log.debug("登陆成功的用户ID:{}", id);

        String username = user.getUsername();
        log.debug("登录成功的用户名：{}", username);

        UserLoginVO ulv = userMapper.selectByUsername(username);
        BeanUtils.copyProperties(ulv,userP);

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        log.debug("登陆成功的用户权限：{}", authorities);

        String authoritiesString = JSON.toJSONString(authorities);
        log.debug("将权限转换为JSON：{}", authoritiesString);

        int userIdentity = SecurityCode.CONSUMER_ID;

        return JwtUtils.setJwt(id, username, authoritiesString, userIdentity, 60 * 24 * 7);
    }

    @Override
    public List<UserListItemVO> list() {
        log.debug("执行查询列表功能！");
        return userMapper.list();
    }

    @Override
    public User getPrinciple() {
        return userP;
    }
}
