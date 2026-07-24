package com.example.login.Config;

import com.example.login.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler customSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // CRITICAL: Used for hashing passwords
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/role-select",
                                "/user/login", "/user/register",
                                "/admin/login", "/admin/register",
                                "/css/**", "/js/**", "/images/**", "/fragments/**"
                        ).permitAll() // Public Access


                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/feedback/admin/**").hasRole("ADMIN")


                        .requestMatchers("/user/**").authenticated()

                        .anyRequest().authenticated() // All others require auth
                )
                .formLogin(form -> form
                        .loginPage("/role-select")      // Redirect here if unauthorized
                        .loginProcessingUrl("/perform_login")
                        .usernameParameter("email")
                        .successHandler(customSuccessHandler)
                        .failureUrl("/role-select?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}