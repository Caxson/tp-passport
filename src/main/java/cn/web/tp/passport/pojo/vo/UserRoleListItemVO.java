package cn.web.tp.passport.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserRoleListItemVO implements Serializable {
    private Long user_id;
    private Long role_id;
}
