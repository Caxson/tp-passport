package cn.web.tp.passport.pojo.vo;

import lombok.Data;

@Data
public class AdminListItemVO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private Integer enable;
}
