package com.demo.config;

import com.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置 (Spring Boot 3.x / Spring Security 6.x 版本)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF
            .csrf(csrf -> csrf.disable())
            // 不使用Session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求认证
            .authorizeHttpRequests(authorize -> authorize
                // 登录和注册接口不需要认证
                .requestMatchers("/api/login", "/api/register").permitAll()
                // Knife4j接口文档不需要认证
                .requestMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                // 静态资源和上传接口不需要认证
                .requestMatchers("/upload/**", "/api/upload/**").permitAll()
                // 商城公开接口不需要认证
                .requestMatchers("/api/banner/**", "/api/category/tree").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/cart/**").permitAll()
                .requestMatchers("/api/product/list", "/api/product/{id}").permitAll()
                .requestMatchers("/api/brand/list").permitAll()
                .requestMatchers("/api/review/list").permitAll()
                .requestMatchers("/api/spec/**").permitAll()
                .requestMatchers("/api/coupon/list").permitAll()
                .requestMatchers("/api/promotion/list").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 添加JWT过滤器
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
