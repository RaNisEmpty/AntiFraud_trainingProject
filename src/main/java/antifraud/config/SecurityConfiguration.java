package antifraud.config;


import antifraud.security.RestAuthenticationEntryPoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfiguration(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .cors().disable().csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests()// manage access
                .antMatchers(HttpMethod.PUT, "/api/auth/role/**", "/api/auth/access/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/api/auth/list").hasAnyRole("ADMINISTRATOR", "SUPPORT")
                .antMatchers(HttpMethod.POST, "/api/antifraud/stolencard").hasRole("SUPPORT")
                .antMatchers(HttpMethod.GET, "/api/antifraud/stolencard").hasRole("SUPPORT")
                .antMatchers(HttpMethod.DELETE, "/api/antifraud/stolencard/**").hasRole("SUPPORT")
                .antMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip").hasRole("SUPPORT")
                .antMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip").hasRole("SUPPORT")
                .antMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/**").hasRole("SUPPORT")
                .antMatchers(HttpMethod.PUT, "/api/antifraud/transaction/**").hasRole("SUPPORT")
                .antMatchers(HttpMethod.GET, "/api/antifraud/history").hasRole("SUPPORT")
                .antMatchers(HttpMethod.GET, "/api/antifraud/history/**").hasRole("SUPPORT")
                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasRole("MERCHANT")
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole("ADMINISTRATOR")
                .antMatchers("/actuator/shutdown").permitAll() // needs to run test
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
        return http.build();
    }
}
