package be.kdg.integration4.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
//    @Bean

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
//                .cors()
                .authorizeHttpRequests(
                        auth -> auth
                        .requestMatchers("/static/**", "/api/customers/**", "/api/save/bike", "/api/staff","/register", "/login", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/api/register", "/api/customer/email", "/api/start-test", "/register", "/login", "/css/**", "/js/**", "/img/**", "/api/workshops/**").permitAll()
                        .requestMatchers("/").hasAnyRole("SUPERADMIN", "ADMIN", "TECHNICIAN", "CUSTOMER")
                        .requestMatchers(
                                "/static/**", "/css/**", "/js/**", "/img/**",  // Static resources
                                "/api/customers/**", "/api/save/bike",        // API endpoints
                                "/register", "/login"                         // Public pages
                        ).permitAll()
                                .anyRequest().authenticated()
                )

//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/error")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException {
                log.info("Authentication : Checking role");
                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TECHNICIAN"))) {
                    log.info("Role detected: {}", authentication.getAuthorities());
                    response.sendRedirect("/technician/dashboard");
                    log.info("Logging into technician dashboard page");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails superadmin = User.withDefaultPasswordEncoder()
//                .username("superadmin@email.com")
//                .password("password")
//                .roles("SUPERADMIN")
//                .build();
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin@email.com")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails technician = User.withDefaultPasswordEncoder()
//                .username("technician@email.com")
//                .password("password")
//                .roles("TECHNICIAN")
//                .build();
//
//        UserDetails customer = User.withDefaultPasswordEncoder()
//                .username("customer@email.com")
//                .password("password")
//                .roles("CUSTOMER")
//                .build();
//
//        return new InMemoryUserDetailsManager(superadmin, admin, technician, customer);
//    }
}
