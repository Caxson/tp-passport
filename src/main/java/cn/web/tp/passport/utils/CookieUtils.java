package cn.web.tp.passport.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public final class CookieUtils {

    public static void addCookies(Boolean rem,String username,String password, HttpServletResponse response,int expireMin){
        //判断是否需要记住
        if(rem!=null){
            log.debug("Cookie用户名：{}",username);
            log.debug("Cookie密码：{}",password);
            //创建Cookie对象并且把用户名和密码装进Cookie 导包javax.servlet
            Cookie c1 = new Cookie("username",username);
            Cookie c2 = new Cookie("password",password);
            //设置保存时间一个月
            c1.setMaxAge(expireMin);
            //把Cookie下发给客户端
            response.addCookie(c1);
            response.addCookie(c2);
        }
    }

}
