package cn.web.tp.passport.exception.handler;

import cn.web.tp.passport.exception.ServiceException;
import cn.web.tp.passport.web.JsonResult;
import cn.web.tp.passport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.StringJoiner;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        log.debug("创建统一处理异常的对象：GlobalExceptionHandler");
    }

    @ExceptionHandler
    public JsonResult handleServiceException(ServiceException e){
        log.error("统一处理ServiceException,将向客户端响应：{}",e.getMessage());
        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public JsonResult handleMyBatis(MyBatisSystemException e){
        log.error("统一处理数据库系统异常,将向客户端响应：{}",e.getMessage());
        String message =  "数据库系统错误！请稍后重试！";
        return JsonResult.fail(ServiceCode.ERR_SQL,message);
    }

    @ExceptionHandler
    public JsonResult handleBindException(BindException e){
        log.error("统一处理BindException,将向客户端响应：{}",e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringJoiner stringJoiner = new StringJoiner(";","WARNING:","!");
        for(FieldError fieldError: fieldErrors){
            stringJoiner.add(fieldError.getDefaultMessage());
        }
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, stringJoiner.toString());
    }

    @ExceptionHandler
    public JsonResult handleAuthentication(AuthenticationException e){
        log.error("统一处理【{}】,将向客户端响应：{}",e.getClass().getName(),e.getMessage());
        return JsonResult.fail(ServiceCode.ERR_UNKNOWN,e.getMessage());
    }

    @ExceptionHandler
    public JsonResult handleThrowable(Throwable e){
        log.error("统一处理【{}】,将向客户端响应：{}",e.getClass().getName(),e.getMessage());
        String message =  "服务器错误！请稍后重试！";
        return JsonResult.fail(ServiceCode.ERR_UNKNOWN,message);
    }
}
