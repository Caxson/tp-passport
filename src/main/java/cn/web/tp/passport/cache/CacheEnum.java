package cn.web.tp.passport.cache;

public enum CacheEnum {

    /**
     * 获取菜单
     */
    GET_CAT(CacheConstants.CAT, CacheConstants.EXPIRES_5_MIN),
    /**
     * 获取菜单列表
     */
    GET_CAT_LIST(CacheConstants.CAT_LIST, CacheConstants.EXPIRES_10_MIN),
    ;
    /**
     * 缓存名称
     */
    private final String name;
    /**
     * 过期时间
     */
    private final int expires;

    /**
     * 构造
     */
    CacheEnum(String name, int expires) {
        this.name = name;
        this.expires = expires;
    }

    public String getName() {
        return name;
    }

    public int getExpires() {
        return expires;
    }
}
