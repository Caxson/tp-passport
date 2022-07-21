package cn.web.tp.passport.filter;

import cn.web.tp.passport.exception.JwtParseException;
import cn.web.tp.passport.security.LoginPrincipal;
import cn.web.tp.passport.utils.JwtUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //此方法是任何请求都会执行的方法
        log.debug("执行doFilterInternal方法");

        //清楚Security的上下文
        // 如果不清除，只要此前存入过信息，即使后续不携带JWT，上下文中的登录信息依然存在
        SecurityContextHolder.clearContext();

        //从请求头中获取JWT
        String jwt = request.getHeader("Authorization");
        log.debug("从请求头中获取JWT数据:{}", jwt);
        if (!StringUtils.hasText(jwt)) {
            log.debug("请求行中数据为空，直接放行");
            filterChain.doFilter(request, response);
            return;
        }

        //如果获取了有效的JWT值。则尝试进行解析
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(JwtUtils.getSecretKey()).parseClaimsJws(jwt).getBody();
        } catch (Throwable e) {
            JwtParseException.handle(e, response);
            return;
        }
        //从解析JWT的结果中取出登陆的管理员信息
        Object id = claims.get("id");
        Object username = claims.get("username");
        Object authoritiesString = claims.get("authorities");
        Object userIdentity = claims.get("userIdentity");
        log.debug("从JWT解析的用户ID：{}", id);
        log.debug("从JWT解析的用户名：{}", username);
        log.debug("从JWT解析得到的权限列表字符串：{}", authoritiesString);
        log.debug("从JWT解析得到的身份特征字符串：{}", userIdentity);

        // 将从JWT中解析得到的权限列表的字符串封装为GrantedAuthority集合
        List<SimpleGrantedAuthority> authorities
                = JSON.parseArray(authoritiesString.toString(), SimpleGrantedAuthority.class);

        //准备当前登录的用户的主体信息
        LoginPrincipal loginPrincipal
                = new LoginPrincipal(
                Long.parseLong(id.toString()),
                username.toString(),
                Integer.parseInt(userIdentity.toString()));

        //当解析成功后，应该将相关数据存入到Spring Security的上下文中
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(loginPrincipal, null, authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        log.debug("已经向Security的上下文中写入：{}", authentication);
        //最终，过滤器可以选择“阻止”或“放行”
        //如果选择阻止，则后续所有组件都不会执行
        //如果选择“放行”，会执行“过滤器链中的剩余部分，甚至继续会向后执行到控制器组件

        // 以下代码将执行“放行”
        filterChain.doFilter(request, response);
    }
}
