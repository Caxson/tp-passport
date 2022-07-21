package cn.web.tp.passport.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserAddNewDTO implements Serializable {

    private String tel;
    private String username;
    private String nickname;
    private String password;
    private List<Long> roleIds; //角色ID
}
