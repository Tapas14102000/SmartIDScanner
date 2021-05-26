package com.TapasCodes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
 @Bean
 public UserDetailsService getUserDetailService() {
	 return new UserDetailsServiceImpl();
 }
 @Bean
 public BCryptPasswordEncoder passwordEncoder() {
	 return new BCryptPasswordEncoder();
 }
 @Bean
 public DaoAuthenticationProvider authenticationProvider() {
	 DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
	 daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
	 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	 return daoAuthenticationProvider;
 }
 
 //configure method...
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.authenticationProvider(authenticationProvider());
}
@Override
protected void configure(HttpSecurity http) throws Exception {
	http
	.authorizeRequests()
	.antMatchers("/user/**").access("hasRole('ROLE_USER')")
	.antMatchers("/**").permitAll()
	.and()
	.formLogin()
	.loginPage("/signin")
	.loginProcessingUrl("/doLogin")
	.defaultSuccessUrl("/user/profile",true)
	.and()
	.csrf().disable();
}

@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry
		.addResourceHandler("/URL/**")
		.addResourceLocations("/SmartIDScanner/src/main/resources/static/");
	}

}
