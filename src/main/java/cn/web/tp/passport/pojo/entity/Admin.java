package cn.web.tp.passport.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Admin implements Serializable {
    private Long id ;//管理员id
    private String username ;    //管理员用户名
    private String password ;    // 密码（密文）
    private String nickname ;    // 昵称
    private String phone ;       // 手机号码
    private String email ;       //电子邮箱
    private String description ; //描述
    private Integer enable ;     //是否启用，1=启用，0=未启用
    private LocalDateTime gmtCreate;     //     数据创建时间
    private LocalDateTime gmtModified;   //   数据最后修改时间


}
