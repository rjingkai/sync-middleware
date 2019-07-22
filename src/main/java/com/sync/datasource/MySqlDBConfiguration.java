package com.sync.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @ClassName: MySqlDBConfiguration
 * @Description: Mysql数据源配置
 * @author NingChongQing
 * @date 2018年4月8日 下午3:15:06
 *       备注：@MapperScan指明了扫描dao层，并且给dao层注入指定的SqlSessionTemplate。所有@Bean都需要按照命名指定正确
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.sync.mysql.model.mapper" }, sqlSessionTemplateRef = "mysqlSessionTemplate")
public class MySqlDBConfiguration extends AbstractDruidDBConfig {
	@Value("${spring.datasource.mysql.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.mysql.url}")
	private String url;

	@Value("${spring.datasource.mysql.username}")
	private String username;

	@Value("${spring.datasource.mysql.password}")
	private String password;

	/**
	 * 注册数据源
	 */
	@Bean(name = "mysqlDataSource", initMethod = "init", destroyMethod = "close")
	@Primary
	public DruidDataSource mysqlDatasource() {
		return super.createDataSource(driverClassName, url, username, password);
	}

	@Bean(name = "mysqlSessionFactory")
	@Primary
	public SqlSessionFactory mysqlSessionFactory(@Qualifier("mysqlDataSource") DruidDataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mysql/mapper/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "mysqlTransactionManager")
	@Primary
	public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DruidDataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "mysqlSessionTemplate")
	@Primary
	public SqlSessionTemplate mysqlSessionTemplate(
			@Qualifier("mysqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
