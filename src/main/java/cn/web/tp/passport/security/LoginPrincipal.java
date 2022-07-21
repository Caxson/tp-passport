package cn.web.tp.passport.security;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
public class LoginPrincipal implements Serializable {
    /**
     * 当前登录的用户id
     */
    private Long id;
    /**
     * 当前登录的用户名
     */
    private String name;
    /**
     * 用户身份特征
     */
    private Integer userIdentity;

    public LoginPrincipal(){

    }
    public LoginPrincipal(String name, Integer userIdentity){
        this.name = name;
        this.userIdentity = userIdentity;
    }
    public LoginPrincipal(Long id, String name, Integer userIdentity){
        this.id = id;
        this.name = name;
        this.userIdentity = userIdentity;
    }
}
