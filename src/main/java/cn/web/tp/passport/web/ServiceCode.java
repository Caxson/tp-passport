package cn.web.tp.passport.web;

public class ServiceCode {
    public static final int OK = 2000;
    public static final int ERR_BAD_REQUEST = 4000;
    public static final int ERR_NOT_FOUND = 4004;
    public static final int ERR_NULL = 4008;
    public static final int ERR_FALSE = 4005;
    public static final int ERR_CONFLICT = 4009;
    public static final int ERR_INSERT = 4010;
    public static final int ERR_DELETE = 4020;
    public static final int ERR_UPDATE = 4030;
    public static final int ERR_READ = 4040;
    /**
     * JWT数据过期
     */
    public static final int ERR_JWT_EXPIRED=4300;
    /**
     * 错误：JWT数据错误，可能被恶意篡改
     */
    public static final int ERR_JWT_INVALID=4310;
    public static final int ERR_SQL = 4888;
    public static final int ERR_UNKNOWN = 5999;
}
