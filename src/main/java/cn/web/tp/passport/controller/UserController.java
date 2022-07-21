package cn.web.tp.passport.controller;

import cn.web.tp.passport.pojo.dto.UserAddNewDTO;
import cn.web.tp.passport.pojo.dto.UserLoginDTO;
import cn.web.tp.passport.pojo.vo.UserListItemVO;
import cn.web.tp.passport.security.LoginPrincipal;
import cn.web.tp.passport.service.IUserService;
import cn.web.tp.passport.web.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    public IUserService userService;

    @PostMapping("/add-new")
    public JsonResult addNew(@RequestBody @Valid UserAddNewDTO userAddNewDTO){
        log.debug("请求到的参数：{}",userAddNewDTO);
        userService.addNew(userAddNewDTO);
        return JsonResult.ok();
    }

    @PostMapping("/login")
    public JsonResult login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response){
        log.debug("请求到的参数：{}", userLoginDTO);
        String jwt = userService.login(userLoginDTO,response);
        log.debug("准备回应的jwt：{}",jwt);
        return JsonResult.ok(jwt);
    }

    @GetMapping("")
    public JsonResult list(@AuthenticationPrincipal LoginPrincipal loginPrincipal){
        log.debug("接收到查询管理员列表的请求");
        log.debug("当前认证信息中的当事人信息：{}",loginPrincipal);
        Long id = loginPrincipal.getId();
        log.debug("从认证信息中获取当前登录的管理员的id：{}",id);
        String username = loginPrincipal.getName();
        log.debug("从认证信息中获取当前登录的管理员的用户名：{}",username);
        List<UserListItemVO> userListItemVOS =  userService.list();
        return JsonResult.ok(userListItemVOS);
    }
}
