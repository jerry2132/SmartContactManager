package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import com.example.userdetails.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//	    provider.setUserDetailsService(customUserDetailsServiceImpl);
//	    provider.setPasswordEncoder(passwordEncoder()); // Make sure to provide your password encoder
//	    return provider;
//	}

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable().authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/static/**").permitAll()
		.requestMatchers("/**").permitAll().and().formLogin().loginPage("/login").loginProcessingUrl("/login")
		.defaultSuccessUrl("/dashboard").failureUrl("/login?error=error")

		.and().logout().logoutUrl("/logout").permitAll();
		
		return http.build();
	}
	
}
//.failureUrl("/login?error")
