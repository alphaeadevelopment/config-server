package uk.alphaea.dev.configserver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JdbcConfiguration {
	
	@Value("${config.server.jdbc.username}") String username;
	@Value("${config.server.jdbc.password}") String password;
	@Value("${config.server.jdbc.driver.class.name}") String driverClassname;
	@Value("${config.server.jdbc.url}") String url;

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driverClassname);
		ds.setUrl(url);
		return ds;
	}

}
