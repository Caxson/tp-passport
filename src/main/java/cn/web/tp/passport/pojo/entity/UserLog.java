package cn.web.tp.passport.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 基于此对象封装用户行为日志？
 * 谁在什么时间执行了什么操作，访问了什么方法，传递了什么参数，访问时长是多少，状态是什么？
 */
@Data
public class UserLog {

    private Long id;
    private Long userId;
    private String ip;
    private String username;
    private String nickname;
    private String tel;
    private Date createdTime;
    private Long time;
    private String operation;
    private String method;
    private String params;
    private Integer status;
    private String error;
}
