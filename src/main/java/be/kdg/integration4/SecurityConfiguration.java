package be.kdg.integration4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails superadmin = User.withDefaultPasswordEncoder()
                .username("superadmin@email.com")
                .password("password")
                .roles("SUPERADMIN")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin@email.com")
                .password("password")
                .roles("ADMIN")
                .build();

        UserDetails technician = User.withDefaultPasswordEncoder()
                .username("technician@email.com")
                .password("password")
                .roles("TECHNICIAN")
                .build();

        UserDetails customer = User.withDefaultPasswordEncoder()
                .username("customer@email.com")
                .password("password")
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(superadmin, admin, technician, customer);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/").hasAnyRole("SUPERADMIN", "ADMIN", "TECHNICIAN", "CUSTOMER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
