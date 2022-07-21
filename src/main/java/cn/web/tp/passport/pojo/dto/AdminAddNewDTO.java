package cn.web.tp.passport.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminAddNewDTO implements Serializable {

    private String username ;    //用户名
    private String password ;    // 密码（密文）
    private String nickname ;    // 昵称
    private String avatar ;      // 头像URL
    private String phone ;       // 手机号码
    private String email ;       //电子邮箱
    private String description ; //描述
    private Integer enable ;     //是否启用，1=启用，0=未启用
    private List<Long> roleIds; //角色ID
}
