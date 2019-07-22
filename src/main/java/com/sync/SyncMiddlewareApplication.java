package com.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* @ClassName: SyncMiddlewareApplication 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author NingChongQing
* @date 2018年4月8日 下午2:39:01
* 备注：
*   将spring boot自带的DataSourceAutoConfiguration禁掉，
*   因为它会读取application.properties文件的spring.datasource.*属性并自动配置单数据源，
*   在@SpringBootApplication注解中添加exclude属性即可
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling
public class SyncMiddlewareApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SyncMiddlewareApplication.class, args);
	}
	
}
