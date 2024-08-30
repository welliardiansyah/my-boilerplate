package com.boilerplate.welli.Created.By.Welli.Ardiansyah.config;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.AuthEntryPointJwt;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.AuthTokenFilter;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/role/**",
            "/api/v1/permission/**",
            "/api/v1/nidi/upload/**",
            "/api/v1/bank/**",
            "/api/v1/disbursement/**",
            "/api/v1/banger/**",
            "/api/v1/mitra/**",
            "/api/v1/periode/**",
            "/api/v1/sanggah/**",
            "/api/v1/margin/**",
            "/api/v1/vd/**",
            "/api/v1/ba-rekon/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(GET, "/api/v1/auth/**").hasAuthority("ADMIN")
                                .requestMatchers(POST, "/api/v1/auth/**").hasAuthority("ADMIN")
                                .requestMatchers(PUT, "/api/v1/auth/**").hasAuthority("ADMIN")
                                .requestMatchers(DELETE, "/api/v1/auth/**").hasAuthority("ADMIN")
                                .requestMatchers(GET, "/api/v1/role/**").hasAuthority("ADMIN")
                                .requestMatchers(POST, "/api/v1/role/**").hasAuthority("ADMIN")
                                .requestMatchers(PUT, "/api/v1/role/**").hasAuthority("ADMIN")
                                .requestMatchers(DELETE, "/api/v1/role/**").hasAuthority("ADMIN")
                                .requestMatchers(GET, "/api/v1/permission/**").hasAuthority("ADMIN")
                                .requestMatchers(POST, "/api/v1/permission/**").hasAuthority("ADMIN")
                                .requestMatchers(PUT, "/api/v1/permission/**").hasAuthority("ADMIN")
                                .requestMatchers(DELETE, "/api/v1/permission/**").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutUrl("/logout").permitAll();

        return http.build();
    }
}
