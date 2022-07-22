package cn.web.tp.passport.aspect;

import cn.web.tp.passport.annotation.RequiredLog;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.entity.Admin;
import cn.web.tp.passport.pojo.entity.AdminLog;
import cn.web.tp.passport.service.IAdminService;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

@Order(1)
@Slf4j
@Aspect
@Component
public class AdminLogAspect {

    @Autowired
    public IAdminService adminService;

    @Pointcut("@annotation(cn.web.tp.passport.annotation.RequiredLog)")
    public void doLog() {
    }

    @Around("doLog()")
    public Object doLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer status = 1;//ok
        String error = "";
        Long time = 0L;
        long t1 = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long t2 = System.currentTimeMillis();
            time = t2 - t1;
            return result;
        } catch (Throwable e) {
            long t3 = System.currentTimeMillis();
            time = t3 - t1;
            status = 0;
            error = e.getMessage();
            throw e;
        } finally {
            saveUserLog(joinPoint, time, status, error);
        }
    }

    private void saveUserLog(ProceedingJoinPoint joinPoint,
                             Long time, Integer status, String error)
            throws NoSuchMethodException, JsonProcessingException {

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = request.getRemoteAddr();

        String username = "";
        String nickname = "";
        Long adminId = 0L;
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        log.debug("管理员信息Session：{}", admin);
        if (admin != null) {
            username = admin.getUsername();
            nickname = admin.getNickname();
            adminId = admin.getId();
        }else {
            admin = adminService.getPrinciple();
            if(admin != null){
                log.debug("管理员信息Service：{}",admin);
                username = admin.getUsername();
                nickname = admin.getNickname();
                adminId = admin.getId();
            }
        }


        Class<?> targetCls = joinPoint.getTarget().getClass();
        log.debug("目标类型：{}", targetCls);

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        log.debug("目标方法：{}", targetMethod);

        RequiredLog requiredLog = targetMethod.getAnnotation(RequiredLog.class);
        String operation = requiredLog.operation();
        log.debug("获取注解对象中operation的值:{}", operation);

        String method = targetCls.getName() + "." + targetMethod.getName();
        log.debug("用户执行的方法：{}", method);

        Object[] args = joinPoint.getArgs();
        log.debug("方法参数：{},长度：{}", args,args.length);

        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if(arg instanceof AdminLoginDTO){
                username=((AdminLoginDTO) arg).getUsername();
            }
            if(arg instanceof HttpServletResponse)continue;
            log.debug("arg:{}", arg);
            if(arg==null)continue;
            params.append(arg.toString());
        }


        log.debug("执行方法传递的参数：{}", params.toString());

        //2.封装用户行为日志
        AdminLog adminLog = new AdminLog();
        adminLog.setIp(ip);
        adminLog.setAdminId(adminId);
        adminLog.setNickname(nickname);
        adminLog.setUsername(username);
        adminLog.setCreatedTime(new Date());
        adminLog.setOperation(operation);
        adminLog.setMethod(method);
        adminLog.setParams(params.toString().toString());
        adminLog.setTime(time);
        adminLog.setStatus(status);
        adminLog.setError(error);
        //3.记录用户行为日志(文件、数据库表、MQ)
        String threadName = Thread.currentThread().getName();
        log.debug("saveUserLog.threadName={}", threadName);

        adminService.insertLog(adminLog);
    }
}
