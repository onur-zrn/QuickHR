package com.quickhr.config;

import com.quickhr.enums.*;
import com.quickhr.enums.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public JWTTokenFilter getJwtTokenFilter() {
        return new JWTTokenFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(new CorsConfig().corsConfigurationSource()))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(
                                    "/swagger-ui/**", "/api-docs/**",
                                    "/v1/dev/user/register", "/v1/dev/user/doLogin",
                                    "/v1/dev/admin/doLogin"
                            ).permitAll()
                            .requestMatchers("/admin/**", "/users/**").hasAuthority(EAdminRole.ADMIN.toString())
                            //.requestMatchers("/admin/**", "/users/**").hasAuthority(EAdminRole.SUPER_ADMIN.toString())
                            .requestMatchers("/manager/**").hasAuthority(EUserRole.MANAGER.toString())
                            .requestMatchers("/personal/**").hasAuthority(EUserRole.PERSONAL.toString())
                            .anyRequest().permitAll();
                });
        http.formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
