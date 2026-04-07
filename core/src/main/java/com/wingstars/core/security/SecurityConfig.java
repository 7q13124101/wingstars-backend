package com.wingstars.core.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("""
                ROLE_SUPER_ADMIN > ROLE_ADMIN
                ROLE_ADMIN > ROLE_USER
                """);
        return roleHierarchy;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            ObjectProvider<AuthenticationRequestFilter> authenticationFilterProvider
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/cheerleaders/**", "/api/files/**", "/api/ranking-categories/**", "/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ranking-categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/ranking-categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ranking-categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/ranking-entries/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/ranking-entries/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/ranking-entries/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/ranking-categories/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**", "/error").permitAll()
                        .anyRequest().authenticated());

        AuthenticationRequestFilter authenticationFilter = authenticationFilterProvider.getIfAvailable();
        if (authenticationFilter != null) {
            http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }
}


