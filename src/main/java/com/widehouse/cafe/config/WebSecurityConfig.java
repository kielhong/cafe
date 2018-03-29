package com.widehouse.cafe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by kiel on 2017. 2. 28..
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/view/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/cafes/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/");
    }
}
