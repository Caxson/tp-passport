package cn.web.tp.passport.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminRoleAddNewVO implements Serializable {
    private Long adminId;//管理员ID

    private Long roleId; //角色ID
}
