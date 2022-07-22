create table tp.tp_admin
(
    id           bigint unsigned auto_increment comment '管理员id'
        primary key,
    username     varchar(20)                null comment '用户名',
    password     char(64)                   null comment '密码',
    phone        varchar(11)                null comment '电话',
    email        varchar(30)                null comment '邮箱',
    nickname     varchar(20)                null comment '昵称',
    description  varchar(255)               null comment '描述',
    enable       tinyint unsigned default 0 null comment '是否启用，1=启用，0=未启用',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '管理员表' charset = utf8mb4;

create table tp.tp_admin_login_log
(
    id           bigint unsigned auto_increment
        primary key,
    admin_id     bigint unsigned null comment '管理员id',
    username     varchar(50)     null comment '管理员用户名（冗余）',
    nickname     varchar(50)     null comment '管理员昵称（冗余）',
    operation    varchar(50)     null comment '管理员操作',
    method       varchar(200)    null comment '请求方法',
    params       varchar(5000)   null comment '请求参数',
    time         bigint          not null comment '执行时长(毫秒)',
    ip           varchar(64)     null comment 'IP地址',
    created_time datetime        null comment '创建时间',
    status       int default 1   null,
    error        varchar(500)    null
)
    comment '管理员日志' charset = utf8mb4;

create table tp.tp_admin_role
(
    id           bigint unsigned auto_increment comment '管理员角色关联表id'
        primary key,
    admin_id     bigint unsigned null comment '管理员id',
    role_id      bigint unsigned null comment '角色id',
    gmt_create   datetime        null comment '数据创建时间',
    gmt_modified datetime        null comment '数据最后修改时间'
)
    comment '管理员角色关联表' charset = utf8mb4;

create table tp.tp_album
(
    id           bigint unsigned auto_increment comment '相册id'
        primary key,
    name         varchar(50) null comment '相册名字',
    gmt_create   datetime    null comment '数据创建时间',
    gmt_modified datetime    null comment '数据最后修改时间'
)
    comment '相册表' charset = utf8mb4;

create table tp.tp_comments
(
    id           bigint unsigned auto_increment comment '评论id'
        primary key,
    user_id      bigint unsigned        null comment '用户id',
    album_id     bigint unsigned        null comment '相册id',
    user_name    varchar(50)            null comment '用户名字',
    content      varchar(255)           null comment '评论内容',
    kudos        int unsigned default 0 null comment '点赞',
    gmt_create   datetime               null comment '数据创建时间',
    gmt_modified datetime               null comment '数据最后修改时间'
)
    comment '评论表' charset = utf8mb4;

create table tp.tp_order
(
    id             bigint unsigned auto_increment comment '订单id'
        primary key,
    user_id        bigint unsigned               null comment '用户id',
    packages_id    bigint unsigned               null comment '套餐id',
    deposit        decimal(10, 2)   default 0.00 null comment '定金',
    payment        decimal(10, 2)   default 0.00 null comment '全款',
    refund         decimal(10, 2)   default 0.00 null comment '退款',
    remarks        varchar(255)                  null comment '订单备注',
    shooting_time  date                          null comment '预约拍摄时间',
    shooting_date  date                          null comment '拍摄周期',
    trip_status    tinyint unsigned default 1    null comment '行程状态(1.未出发 2.旅拍中 3.行程结束)',
    payment_status tinyint unsigned default 1    null comment '付款状态(1.未付款 2.定金已付 3.已付全款 )',
    refund_status  tinyint unsigned default 1    null comment '退款状态(1.未退款 2.审核中 3.退款成功 )',
    gmt_create     datetime                      null comment '数据创建时间',
    gmt_modified   datetime                      null comment '数据最后修改时间'
)
    comment '订单表' charset = utf8mb4;

create table tp.tp_packages
(
    id              bigint unsigned auto_increment comment '套餐id'
        primary key,
    style_id        bigint unsigned null comment '风格id',
    route_id        bigint unsigned null comment '路线id',
    photographer_id bigint unsigned null comment '摄影师id',
    negatives       int unsigned    null comment '底片',
    refinement      int unsigned    null comment '精修',
    gmt_create      datetime        null comment '数据创建时间',
    gmt_modified    datetime        null comment '数据最后修改时间'
)
    comment '套餐表' charset = utf8mb4;

create table tp.tp_permission
(
    id           bigint unsigned auto_increment comment '权限id'
        primary key,
    name         varchar(50)                null comment '名称',
    value        varchar(255)               null comment '值',
    description  varchar(255)               null comment '描述',
    sort         tinyint unsigned default 0 null comment '自定义排序序号',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '权限表' charset = utf8mb4;

create table tp.tp_photographer
(
    id           bigint unsigned auto_increment comment '摄影师id'
        primary key,
    name         varchar(50)                null comment '摄影师名字',
    description  varchar(255)               null comment '摄影师简介',
    enable       tinyint unsigned default 0 null comment '是否启用，1=启用，0=未启用',
    album_id     bigint unsigned            null comment '相册id',
    is_slot      tinyint unsigned           null comment '摄影师档期安排',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '摄影师表' charset = utf8mb4;

create table tp.tp_picture
(
    id           bigint unsigned auto_increment comment '图片id'
        primary key,
    url          varchar(255)               null comment '图片路径',
    is_cover     tinyint unsigned default 0 null comment '设置封面图片',
    album_id     bigint unsigned            null comment '相册id',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '图片表' charset = utf8mb4;

create table tp.tp_role
(
    id           bigint unsigned auto_increment comment '角色表id'
        primary key,
    name         varchar(50)                null comment '名称',
    description  varchar(255)               null comment '描述',
    sort         tinyint unsigned default 0 null comment '自定义排序序号',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '角色表' charset = utf8mb4;

create table tp.tp_role_permission
(
    id            bigint unsigned auto_increment comment '角色权限关联id'
        primary key,
    role_id       bigint unsigned null comment '角色id',
    permission_id bigint unsigned null comment '权限id',
    gmt_create    datetime        null comment '数据创建时间',
    gmt_modified  datetime        null comment '数据最后修改时间'
)
    comment '角色权限关联表' charset = utf8mb4;

create table tp.tp_route
(
    id           bigint unsigned auto_increment comment '路线id'
        primary key,
    name         varchar(50)                null comment '路线名字',
    keywords     varchar(255)               null comment '路线关键字',
    description  varchar(255)               null comment '路线简介',
    album_id     bigint unsigned            null comment '相册id',
    enable       tinyint unsigned default 0 null comment '是否启用，1=启用，0=未启用',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '拍摄路线表' charset = utf8mb4;

create table tp.tp_store
(
    id           bigint unsigned auto_increment comment '门店id'
        primary key,
    name         varchar(255) null comment '门店名字',
    city         varchar(255) null comment '所在城市',
    gmt_create   datetime     null comment '数据创建时间',
    gmt_modified datetime     null comment '数据最后修改时间'
)
    comment '门店表' charset = utf8mb4;

create table tp.tp_style
(
    id           bigint unsigned auto_increment comment '风格id'
        primary key,
    name         varchar(50)                null comment '风格名字',
    keywords     varchar(255)               null comment '风格关键字',
    description  varchar(255)               null comment '风格简介',
    enable       tinyint unsigned default 0 null comment '是否启用，1=启用，0=未启用',
    album_id     bigint unsigned            null comment '相册id',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '拍摄风格表' charset = utf8mb4;

create table tp.tp_user
(
    id           bigint unsigned auto_increment comment '用户id'
        primary key,
    tel          varchar(50)                null comment '用户手机号',
    username     varchar(50)                null comment '用户名字',
    nickname     varchar(50)                null comment '用户昵称',
    password     char(64)                   null comment '密码',
    enable       tinyint unsigned default 0 null comment '是否启用，1=启用，0=未启用',
    gmt_create   datetime                   null comment '数据创建时间',
    gmt_modified datetime                   null comment '数据最后修改时间'
)
    comment '用户表' charset = utf8mb4;

create table tp.tp_user_login_log
(
    id           bigint unsigned auto_increment
        primary key,
    user_id      bigint unsigned null comment '用户id',
    username     varchar(50)     null comment '用户用户名（冗余）',
    nickname     varchar(50)     null comment '用户昵称（冗余）',
    tel          varchar(50)     null comment '用户手机号',
    operation    varchar(50)     null comment '用户操作',
    method       varchar(200)    null comment '请求方法',
    params       varchar(5000)   null comment '请求参数',
    time         bigint          not null comment '执行时长(毫秒)',
    ip           varchar(64)     null comment 'IP地址',
    created_time datetime        null comment '创建时间',
    status       int default 1   null,
    error        varchar(500)    null
)
    comment '用户日志' charset = utf8mb4;

create table tp.tp_user_role
(
    id           bigint unsigned auto_increment comment '用户角色关联表id'
        primary key,
    user_id      bigint unsigned null comment '用户id',
    role_id      bigint unsigned null comment '角色id',
    gmt_create   datetime        null comment '数据创建时间',
    gmt_modified datetime        null comment '数据最后修改时间'
)
    comment '用户角色关联表' charset = utf8mb4;