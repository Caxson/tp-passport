package cn.web.tp.passport.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserListItemVO implements Serializable {
    private Long id;
    private String tel;
    private String username;
    private String nickname;
    private String password;
}
