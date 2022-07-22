package cn.web.tp.passport.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> list = new ArrayList<>();
        //循环添加枚举类中自定义的缓存
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            list.add(new CaffeineCache(cacheEnum.getName(),
                    Caffeine.newBuilder()
                            .initialCapacity(50)
                            .maximumSize(1000) //缓存的最大条数
                            .expireAfterWrite(cacheEnum.getExpires(), TimeUnit.SECONDS)
                            .build()));
        }
        cacheManager.setCaches(list);
        return cacheManager;
    }
}


