package com.user.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {

    private final JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors ->cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/user/api/v1/**").permitAll()
                        .requestMatchers("/user/api/auth/v1/**").authenticated()
                                .requestMatchers("/user/test/**").permitAll()
//                                .anyRequest().permitAll()
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, authException) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cors=new CorsConfiguration();
        cors.setAllowedOriginPatterns(List.of("*"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cors);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){return new BCryptPasswordEncoder();}

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate(){
//        return new RestTemplate();
//    }
}
