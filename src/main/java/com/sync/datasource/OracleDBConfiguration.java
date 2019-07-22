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
 * @ClassName: OracleDBConfiguration
 * @Description: Oralce数据源配置
 * @author NingChongQing
 * @date 2018年4月8日 下午3:15:00
 *       备注：@MapperScan指明了扫描dao层，并且给dao层注入指定的SqlSessionTemplate。所有@Bean都需要按照命名指定正确
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.sync.oraclel.model.mapper" }, sqlSessionTemplateRef = "oracleSessionTemplate")
public class OracleDBConfiguration extends AbstractDruidDBConfig {
	@Value("${spring.datasource.oracle.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.oracle.url}")
	private String url;

	@Value("${spring.datasource.oracle.username}")
	private String username;

	@Value("${spring.datasource.oracle.password}")
	private String password;

	/**
	 * 注册数据源
	 */
	@Bean(name = "oracleDataSource", initMethod = "init", destroyMethod = "close")
	@Primary
	public DruidDataSource oracleDataSource() {
		return super.createDataSource(driverClassName, url, username, password);
	}

	@Bean(name = "oracleSessionFactory")
	@Primary
	public SqlSessionFactory oracleSessionFactory(@Qualifier("oracleDataSource") DruidDataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/oracle/mapper/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "oracleTransactionManager")
	@Primary
	public DataSourceTransactionManager oracleTransactionManager(@Qualifier("oracleDataSource") DruidDataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "oracleSessionTemplate")
	@Primary
	public SqlSessionTemplate oracleSessionTemplate(
			@Qualifier("oracleSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
