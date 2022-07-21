package cn.web.tp.passport.config;

import cn.web.tp.passport.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("创建密码编辑器组件：BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();//返回BCrypt密码编辑器组件对象
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //在配置路径时，可以使用星号作为通配符
        //使用/*只能匹配一层路径，例如/user或/brand，不可以匹配多层级，例如不可以匹配到/user/list
        //使用/**可以匹配到若干层路径

        //白名单，不需要登录就可以访问
        String[] urls = {
                "/admins/login",
                "/users/login",
                "/doc.html",
                "/**/*.js",
                "/**/*.css",
                "/swagger-resources",
                "/v2/api-docs",
                "/favicon.ico"
        };
        //添加处理JWT的过滤器，必须在处理用户名、密码的过滤器（Spring security内置）之前
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors();//放行复杂的异步请求的预检，跨域资源共享，放行Options，本质时在过滤器链中添加CorsFilter
        http.csrf().disable();//禁用防止跨域伪造的攻击，如果无此配置，白名单路径的异步访问也会出现403错误

        http.authorizeRequests()//请求需要被授权才可以被访问
                .antMatchers(HttpMethod.OPTIONS,"/**")//允许options访问
                .permitAll()//允许此前的antMatchers()的配置的路径的所有方法
                .antMatchers(urls)//匹配某些路径
                .permitAll()//可以直接访问（不需要经过认证和授权）
                .anyRequest()//除了以上配置的所有请求
                .authenticated();//已经认证通过的，即已经登陆过的才可以访问
    }
}
