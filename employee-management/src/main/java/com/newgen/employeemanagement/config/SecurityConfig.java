package com.newgen.employeemanagement.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails rahul =
                User.builder()
                        .username("rahul")
                        .password("{noop}rahul123")
                        .roles("EMPLOYEE")
                        .build();

        UserDetails asha =
                User.builder()
                        .username("asha")
                        .password("{noop}asha123")
                        .roles("EMPLOYEE", "MANAGER")
                        .build();

        UserDetails admin =
                User.builder()
                        .username("Revati")
                        .password("{noop}revati123")
                        .roles("EMPLOYEE", "MANAGER", "ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(rahul, asha, admin);

    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.authorizeHttpRequests(
                configurer ->
                        configurer
                                .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasRole("EMPLOYEE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/employees").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/employees").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employee/report").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/employees/**").hasRole("ADMIN")
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .anyRequest().denyAll());

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }




}
