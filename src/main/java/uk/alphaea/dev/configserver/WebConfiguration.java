package uk.alphaea.dev.configserver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfiguration {
	
	@Value("${cors.allowed.origins.regex}") String corsAllowedOriginsRegex;

	@Bean
	CorsProcessor corsProcessor() {
		return new DefaultCorsProcessor();
	}

	@Bean
	CorsConfiguration corsConfiguration() {
		final Pattern p = Pattern.compile(corsAllowedOriginsRegex);

		CorsConfiguration config = new CorsConfiguration() {
			public String checkOrigin(String requestOrigin) {
				if (!StringUtils.hasText(requestOrigin)) {
					return null;
				}
				Matcher matcher = p.matcher(requestOrigin);
				return (matcher.matches() ? requestOrigin : null);
			}
		};
		config.setAllowCredentials(true);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		return config;
	}

	@Bean
	public FilterRegistrationBean corsFilter(CorsConfiguration config, CorsProcessor corsProcessor) {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		CorsFilter corsFilter = new CorsFilter(source);
		corsFilter.setCorsProcessor(corsProcessor);
		FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
		bean.setOrder(0);
		return bean;
	}
}
