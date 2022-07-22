package cn.web.tp.passport.aspect;

import cn.web.tp.passport.annotation.RequiredLog;
import cn.web.tp.passport.annotation.RequiredLogU;
import cn.web.tp.passport.pojo.dto.AdminLoginDTO;
import cn.web.tp.passport.pojo.dto.UserLoginDTO;
import cn.web.tp.passport.pojo.entity.Admin;
import cn.web.tp.passport.pojo.entity.AdminLog;
import cn.web.tp.passport.pojo.entity.User;
import cn.web.tp.passport.pojo.entity.UserLog;
import cn.web.tp.passport.service.IAdminService;
import cn.web.tp.passport.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
@Order(2)
@Aspect
@Component
public class UserLogAspect {

    @Autowired
    private IUserService userService;

    @Pointcut("@annotation(cn.web.tp.passport.annotation.RequiredLogU)")
    public void doLog(){}

    @Around("doLog()")
    public Object doLogAround(ProceedingJoinPoint joinPoint)throws  Throwable{
        Integer status=1;//ok
        String error="";
        Long time=0L;
        long t1=System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long t2=System.currentTimeMillis();
            time=t2-t1;
            return result;
        }catch (Throwable e){
            long t3=System.currentTimeMillis();
            time=t3-t1;
            status=0;
            error=e.getMessage();
            throw e;
        }finally{
                saveUserLog(joinPoint,time,status,error);
        }
    }
    private void saveUserLog(ProceedingJoinPoint joinPoint,
                             Long time, Integer status, String error)
            throws NoSuchMethodException, JsonProcessingException {

        ServletRequestAttributes requestAttributes=
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String ip=request.getRemoteAddr();

        String username="";
        String nickname="";
        String tel="";
        Long userId=0L;
        User user= (User) request.getSession().getAttribute("user");
        log.debug("用户信息：{}", user);
        if(user!=null){
            username=user.getUsername();
            nickname=user.getNickname();
            userId=user.getId();
        }else {
            user = userService.getPrinciple();
            if(user != null){
                log.debug("管理员信息Service：{}",user);
                username = user.getUsername();
                nickname = user.getNickname();
                userId = user.getId();
                tel = user.getTel();
            }
        }

        Class<?> targetCls=joinPoint.getTarget().getClass();
        MethodSignature ms= (MethodSignature) joinPoint.getSignature();
        Method targetMethod=targetCls.getDeclaredMethod(ms.getName(),ms.getParameterTypes());

        RequiredLogU requiredLog = targetMethod.getAnnotation(RequiredLogU.class);
        String operation=requiredLog.operation();
        String method=targetCls.getName()+"."+targetMethod.getName();

        Object[] args = joinPoint.getArgs();
        log.debug("方法参数：{},长度：{}", args,args.length);

        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if(arg instanceof UserLoginDTO){
                username=((UserLoginDTO) arg).getUsername();
            }
            log.debug("arg:{}", arg);
            params.append(arg.toString());
        }
        log.debug("执行方法传递的参数：{}", params.toString());

        UserLog userLog=new UserLog();
        userLog.setIp(ip);
        userLog.setTel(tel);
        userLog.setUserId(userId);
        userLog.setNickname(nickname);
        userLog.setUsername(username);
        userLog.setCreatedTime(new Date());
        userLog.setOperation(operation);
        userLog.setMethod(method);
        userLog.setParams(params.toString().toString());
        userLog.setTime(time);
        userLog.setStatus(status);
        userLog.setError(error);
        //3.记录用户行为日志(文件、数据库表、MQ)
        String threadName = Thread.currentThread().getName();
        log.debug("saveUserLog.threadName={}", threadName);

        userService.insertLog(userLog);
    }
}
