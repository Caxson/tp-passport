package cn.web.tp.passport.security;

import cn.web.tp.passport.exception.ServiceException;
import cn.web.tp.passport.mapper.AdminMapper;
import cn.web.tp.passport.mapper.UserMapper;
import cn.web.tp.passport.pojo.vo.AdminLoginVO;
import cn.web.tp.passport.pojo.vo.UserLoginVO;
import cn.web.tp.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UseDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) {

        log.debug("Spring Security通过UserDetails自动根据用户身份【{}】处理登录", s);

        LoginPrincipal loginPrincipal = JSON.parseObject(s, LoginPrincipal.class);
        int userIdentity = loginPrincipal.getUserIdentity();
        String name = loginPrincipal.getName();
        log.debug("用户名：{}，身份特征：{}",name,userIdentity);

        switch (userIdentity) {
            case SecurityCode.ADMIN_ID:

                AdminLoginVO adminLoginVO = adminMapper.selectByUsername(name);
                log.debug("查询到的管理员信息：{}", adminLoginVO);

                if (adminLoginVO != null) {

                    log.debug("查询到匹配的管理员信息：{}", adminLoginVO);

                    List<SimpleGrantedAuthority> authorities
                            = handlerAuthorities(adminLoginVO.getPermissionsValues());

                    AdminDetails adminDetails = new AdminDetails(
                            adminLoginVO.getUsername(),
                            adminLoginVO.getPassword(),
                            adminLoginVO.getEnable() == 1,
                            authorities
                    );
                    adminDetails.setId(adminLoginVO.getId());
                    log.debug("即将向Spring Security返回UserDetails:{}", adminDetails);
                    return adminDetails;
                }
                throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "用户名不存在！");

            case SecurityCode.CONSUMER_ID:

                UserLoginVO userLoginVO = userMapper.selectByUsername(name);
                log.debug("查询到的用户信息：{}", userLoginVO);

                if (userLoginVO != null) {

                    log.debug("查询到匹配的用户信息：{}", userLoginVO);

                    List<SimpleGrantedAuthority> authorities
                            = handlerAuthorities(userLoginVO.getPermissionsValues());

                    ConsumerDetails consumerDetails = new ConsumerDetails(
                            userLoginVO.getUsername(),
                            userLoginVO.getPassword(),
                            userLoginVO.getEnable() == 1,
                            authorities
                    );

                    consumerDetails.setId(userLoginVO.getId());
                    log.debug("即将向Spring Security返回UserDetails:{}", consumerDetails);
                    return consumerDetails;
                }
                throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "用户名不存在！");
            default:
                throw new ServiceException(ServiceCode.ERR_FALSE, "用户类型错误！");
        }

    }

    /**
     * 向Security中写入权限
     * @param permissions
     * @return
     */
    private List<SimpleGrantedAuthority> handlerAuthorities(List<String> permissions) {

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
            return authorities;
    }
}
