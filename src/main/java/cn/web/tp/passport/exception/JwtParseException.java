package cn.web.tp.passport.exception;


import cn.web.tp.passport.web.JsonResult;
import cn.web.tp.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtParseException {

    private static void responseErr(Integer serviceCode,String errMessage,HttpServletResponse response) throws IOException {
        JsonResult jsonResult = JsonResult.fail(serviceCode, errMessage);
        String jsonResultString = JSON.toJSONString(jsonResult);
        log.debug("将向客户端响应：{}",jsonResultString);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().println(jsonResultString);
    }

    public static void handle(Throwable e, HttpServletResponse response) throws IOException {
        log.debug("解析JWT失败：{}，{}",e.getClass().getName(),e.getMessage());
        String[] eArr = e.getClass().getName().split("\\.");
        String err = eArr[eArr.length-1];
        log.debug("解析JWT的异常类型：{}",err);
        switch (err){
            case "ExpiredJwtException":
                responseErr(ServiceCode.ERR_JWT_EXPIRED,"登录信息已过期，请重新登录！",response);
                break;
            case "MalformedJwtException":
            case "SignatureException":
                responseErr(ServiceCode.ERR_JWT_INVALID,"登录信息异常，请重新登录！",response);
                break;
            default:
                responseErr(ServiceCode.ERR_UNKNOWN,"登录异常，请重新登录！",response);
                break;
        }
    }

}
