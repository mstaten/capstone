package com.staten.capstone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/** using Java configuration **/

@Configuration
@ComponentScan(basePackages = {"com.staten.capstone"})
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    public AppSecurityConfig() {}

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception  {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/resources", "/static/**", "/images/**", "/css/**", "/js/**",
                                    "/register", "/unauthorized", "/", "/report/*", "/report/list/**",
                                    "/localreports", "/localreports/sort**", "/about").permitAll()
                    //.antMatchers("/unauthorized/**").hasRole("USER")
                    //.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                        .defaultSuccessUrl("/")
                        //.failureHandler(customAuthenticationHandler())
                        .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    //.logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .and()
                .sessionManagement()
                    .invalidSessionUrl("/invalidSession")
                    .sessionFixation().migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .maximumSessions(2)
                    .expiredUrl("/expiredSession")
        ;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    // Beans

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

        /** first step in enabling concurrent session-control support :
         *  Bean is essential to make sure Spring Security Session registry
         *  is notified when the session is destroyed */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


}
