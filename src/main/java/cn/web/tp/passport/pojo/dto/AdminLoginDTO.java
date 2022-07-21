package cn.web.tp.passport.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminLoginDTO implements Serializable {
    private String username ;    //用户名
    private String password ;    // 密码（密文）
    private Boolean rem;   //是否记住密码
}
