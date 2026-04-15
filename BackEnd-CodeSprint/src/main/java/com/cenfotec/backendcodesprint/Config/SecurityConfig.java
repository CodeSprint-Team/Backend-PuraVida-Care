package com.cenfotec.backendcodesprint.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET,    "/profiles/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/profiles/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,    "/profiles/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/profiles/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH,  "/profiles/**").permitAll()
                        .requestMatchers("/verifications/**").permitAll()
                        .requestMatchers("/profiles/**").permitAll()
                        .requestMatchers("/bookings/**").permitAll()
                        .requestMatchers("/tracking/**").permitAll()

                        .requestMatchers("/simulation/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/ws").permitAll()

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/users/**").permitAll()

                        .requestMatchers("/support-products/**").permitAll()
                        .requestMatchers("/reviews/**").permitAll()
                        .requestMatchers("/services/**").permitAll()

                        .requestMatchers(HttpMethod.GET,    "/booking/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/booking/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,    "/booking/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH,  "/booking/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/booking/**").permitAll()
                        .requestMatchers("/agenda-cliente/**").permitAll()
                        .requestMatchers("/search/**").permitAll()
                        .requestMatchers("/support-product-catalogs/**").permitAll()
                        .requestMatchers("/service-categories/**").permitAll()
                        .requestMatchers("/telemedicina-controll/**").permitAll()
                        .requestMatchers("/ai/**").permitAll()


                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}


