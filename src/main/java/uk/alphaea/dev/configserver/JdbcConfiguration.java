package uk.alphaea.dev.configserver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.server.environment.JdbcEnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JdbcConfiguration {
	
	@Value("${config.server.jdbc.username}") String username;
	@Value("${config.server.jdbc.password}") String password;
	@Value("${config.server.jdbc.driver.class.name}") String driverClassname;
	@Value("${config.server.jdbc.url}") String url;

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		System.out.println("XXXXX"+ System.getProperty("env"));
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driverClassname);
		ds.setUrl(url);
		return ds;
	}
//
//	@Bean
//	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
//		JdbcTemplate jt = new JdbcTemplate(dataSource);
//		return jt;
//	}
//
//	@Bean
//	JdbcTemplate getJdbcTemplate() {
//		return new JdbcTemplate();
//	}
//
//	@Bean
//	JdbcEnvironmentRepository getJdbcEnvironmentRepo(JdbcTemplate jdbcTemplate) {
//		JdbcEnvironmentRepository repo = new JdbcEnvironmentRepository(jdbcTemplate);
//		return repo;
//	}

}
