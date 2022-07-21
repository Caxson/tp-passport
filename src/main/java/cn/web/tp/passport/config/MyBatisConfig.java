package cn.web.tp.passport.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.web.tp.passport.mapper")
public class MyBatisConfig {

}
