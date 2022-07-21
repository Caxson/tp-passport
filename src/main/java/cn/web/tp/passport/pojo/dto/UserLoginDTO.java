package cn.web.tp.passport.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserLoginDTO implements Serializable {

    private String username;
    private String password;
    private Boolean rem;   //是否记住密码
}
