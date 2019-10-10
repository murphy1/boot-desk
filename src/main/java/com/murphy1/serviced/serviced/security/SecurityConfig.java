package com.murphy1.serviced.serviced.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                .antMatchers("/users/delete").hasAuthority("ADMIN")
                .antMatchers("/issues/delete/*").hasAuthority("ADMIN")
                .antMatchers("/service_requests/delete/*").hasAuthority("ADMIN")
                .antMatchers("/issues").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/issues/new").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/issues/update/*").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/service_requests").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/service_requests/new").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/service_requests/update/*").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/profile").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/search").hasAnyAuthority("ADMIN", "AGENT", "END_USER")
                .antMatchers("/console/**").hasAuthority("ADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin().loginPage("/login");

        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
