package cn.web.tp.passport.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminLoginVO implements Serializable {
    private Long id ;//管理员id
    private String username ;    //用户名
    private String password ;    // 密码（密文）
    private Integer enable ;     //是否启用，1=启用，0=未启用
    private List<String> permissionsValues;  //此账号的权限列表
}
