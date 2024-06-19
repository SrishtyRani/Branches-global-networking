package com.branches.Branches.global.networking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;





@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private DetailService userDetailService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        logger.info("Configuring SecurityFilterChain");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                	registry.requestMatchers("/api/v2/**").permitAll();
                	registry.requestMatchers("/password-forgot/**").permitAll();
                	registry.requestMatchers("/sendotp/**").permitAll();
                	registry.requestMatchers("/verifyotp/**").permitAll();
                	registry.requestMatchers("/changepassword").permitAll();
                	registry.requestMatchers("/save").permitAll();
                    registry.requestMatchers("/static/**", "/assets/**").permitAll();
                    registry.requestMatchers("/img/**").permitAll();
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/user/**").hasRole("USER");
                    registry.anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/login")
                            .successHandler(new SuccessHandler())
                            .permitAll();
                    
                })
                
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login")
						.permitAll())
                .build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        logger.info("Initializing UserDetailsService bean");
        return userDetailService;
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        logger.info("Initializing AuthenticationProvider bean");
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        logger.info("Initializing BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }
}

