package com.example.webserviceindastrialproduct.config;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class Security{
    UserDetailsServiceImplementation userDetailsServiceImplementation;
    private BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> request
                        .requestMatchers("/inpr/**", "/about/**","/images/**","/basket/**","/product/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/registr","/styles/**")
                        .permitAll().anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/auth")
                        .permitAll()
                        .defaultSuccessUrl("/inpr").permitAll())
                .logout((logout) -> logout.permitAll());
        return http.build();
    }
    //отвечает за авторизацию с бд
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsServiceImplementation);
        dao.setPasswordEncoder(encoder());
        return dao;
    }

}

