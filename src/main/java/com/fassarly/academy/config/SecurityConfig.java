package com.fassarly.academy.config;

import com.fassarly.academy.auth.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    @EnableMethodSecurity
    public class SecurityConfiguration {

        private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"};
        private final JwtAuthFilter jwtAuthFilter;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(req ->
                            req
                                    // Permit access to certain URLs without authentication
                                    .requestMatchers(WHITE_LIST_URL).permitAll()
                                    // Additional antMatchers or requestMatchers can be added here
                                    .requestMatchers("/api/v1/auth/**").permitAll()
                                    .requestMatchers("/api/roles/getAllRoles").permitAll()
                                    .requestMatchers("/api/orange/**").permitAll()
                                    .requestMatchers("/api/orange/**").permitAll()



                                    // Allow any other request only if authenticated
                                    .requestMatchers("/api/utilisateur/findByNumeroTel/**").authenticated()
                                    .requestMatchers("/api/matiere/findMatiereByUser/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/offers/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/matiere/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/examen/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/lesson/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/prototypeExam/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/seanceEnLigne/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/theme/**").authenticated()
                                    .requestMatchers(HttpMethod.GET, "/api/utilisateur/**").authenticated()
                                    .requestMatchers(HttpMethod.PUT, "/api/utilisateur/**").authenticated()



             /********************************************************Admin Apis********************************************************/
                                    .requestMatchers("/api/offers/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(" /api/comptabilite/**").hasAnyAuthority(new String[]{"ADMIN"})

                                    .requestMatchers(HttpMethod.POST, "/api/offers/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/offers/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/offers/**").hasAnyAuthority(new String[]{"ADMIN"})

                                    .requestMatchers(HttpMethod.POST, "/api/matiere/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/matiere/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/matiere/**").hasAnyAuthority(new String[]{"ADMIN"})


                                    .requestMatchers(HttpMethod.POST, "/api/examen/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/examen/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/examen/**").hasAnyAuthority(new String[]{"ADMIN"})


                                    .requestMatchers(HttpMethod.POST, "/api/lesson/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/lesson/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/lesson/**").hasAnyAuthority(new String[]{"ADMIN"})


                                    .requestMatchers(HttpMethod.POST, "/api/prototypeExam/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/prototypeExam/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/prototypeExam/**").hasAnyAuthority(new String[]{"ADMIN"})

                                    .requestMatchers(HttpMethod.POST, "/api/seanceEnLigne/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/seanceEnLigne/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/seanceEnLigne/**").hasAnyAuthority(new String[]{"ADMIN"})


                                    .requestMatchers(HttpMethod.POST, "/api/theme/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.PUT, "/api/theme/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/theme/**").hasAnyAuthority(new String[]{"ADMIN"})

                                    .requestMatchers(HttpMethod.POST, "/api/utilisateur/**").hasAnyAuthority(new String[]{"ADMIN"})
                                    .requestMatchers(HttpMethod.DELETE, "/api/utilisateur/**").hasAnyAuthority(new String[]{"ADMIN"})

                                    .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.maximumSessions(1))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }



        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;

        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
    }
}
