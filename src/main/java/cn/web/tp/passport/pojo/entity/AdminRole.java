package cn.web.tp.passport.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdminRole implements Serializable {
    private Long id;
    private Long adminId;//管理员ID
    private Long roleId; //角色ID
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
