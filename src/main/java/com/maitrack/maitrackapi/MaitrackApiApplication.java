package com.maitrack.maitrackapi;

import com.maitrack.maitrackapi.filters.AuthFilter;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableConfigurationProperties(JWTProperties.class)
public class MaitrackApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaitrackApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}
	@Bean //
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {

		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);

		//Apply this filter to protected resources (So everything except for login and register should be protected)
		registrationBean.addUrlPatterns("/api/jobs/*");
		registrationBean.addUrlPatterns("/api/users/getUser");
		return registrationBean;
	}

}
