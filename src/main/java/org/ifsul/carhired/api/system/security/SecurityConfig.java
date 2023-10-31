package org.ifsul.carhired.api.system.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.ifsul.carhired.api.user.Permission.*;
import static org.ifsul.carhired.api.user.Role.ADMIN;
import static org.ifsul.carhired.api.user.Role.CUSTOMER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private static final String baseUrl = "/api/v1";

    @Bean
    public SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        .requestMatchers(GET,baseUrl + "/test").permitAll()
                        .requestMatchers(POST,baseUrl + "/test").permitAll()

                        .requestMatchers(POST, baseUrl + "/auth/**").permitAll()
                        .requestMatchers(GET, baseUrl + "/admin/**").hasRole(ADMIN.name())

                        .requestMatchers(GET, baseUrl + "/customer").hasAnyAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(GET, baseUrl + "/customer/**").hasAnyAuthority(CUSTOMER_READ.getPermission())
                        .requestMatchers(PATCH, baseUrl + "/customer/**").hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                        .requestMatchers(DELETE, baseUrl + "/customer/**").hasAnyAuthority(CUSTOMER_DELETE.getPermission())

                        .requestMatchers(baseUrl + "/model/**").hasRole(ADMIN.name())
                        .requestMatchers(baseUrl + "/vehicle/**").hasRole(ADMIN.name())
                        .requestMatchers(baseUrl + "/automaker/**").hasRole(ADMIN.name())

                        .requestMatchers(POST, baseUrl + "/rental/**").hasRole(CUSTOMER.name())
                        .requestMatchers(GET, baseUrl + "/rental/**").hasAnyRole(ADMIN.name(), CUSTOMER.name())
                        .requestMatchers(PATCH, baseUrl + "/rental/**").hasAnyAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, baseUrl + "/rental/**").hasAnyAuthority(ADMIN_DELETE.getPermission())


                )
                .exceptionHandling(exh -> exh
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

}