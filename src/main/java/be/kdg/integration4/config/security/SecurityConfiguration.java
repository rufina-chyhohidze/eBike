package be.kdg.integration4.config.security;


import be.kdg.integration4.controller.mvc.CustomAuthenticationFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/static/**", "/register", "/login", "/css/**", "/js/**", "/img/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/staff").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/workshops").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        //.failureUrl("/login?error")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/ws/**") // Only disable CSRF for WebSocket
                )
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint(
                                (request, response, authException) -> {
                                    if (request.getRequestURI().startsWith("/api")) {
                                        response.setStatus(HttpStatus.FORBIDDEN.value());
                                    } else {
                                        log.debug("Forbidden: {}", request.getRequestURI());
                                        response.sendRedirect("/login");
                                    }
                                }));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
