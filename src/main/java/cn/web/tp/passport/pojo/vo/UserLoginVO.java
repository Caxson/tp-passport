package cn.web.tp.passport.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserLoginVO implements Serializable {
    private Long id;
    private String tel;
    private String username;
    private String nickname;
    private String password;
    private Integer enable;
    private List<String> permissionsValues;  //此账号的权限列表
}
