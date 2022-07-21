package cn.web.tp.passport.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private Long id;
    private String tel;
    private String username;
    private String nickname;
    private String password;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
