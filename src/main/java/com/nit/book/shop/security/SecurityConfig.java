package com.nit.book.shop.security;


import com.nit.book.shop.security.handle.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
            .formLogin()
            .loginPage("/login")
            .failureHandler(failureHandler)
//            .loginProcessingUrl("/")
            .and()
            .logout()
            .logoutSuccessUrl("/");

        http
            .authorizeRequests()
            .and().logout().permitAll()
            .and().authorizeRequests().antMatchers("/admin/**").authenticated()
            .and().authorizeRequests()
            .antMatchers("/**").permitAll();

        http
            .sessionManagement()
            .maximumSessions(1);
        //退出时情况cookies
        http
            .logout()
            .deleteCookies("JESSIONID");

        //解决中文乱码问题
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("222222"));

    }
}
