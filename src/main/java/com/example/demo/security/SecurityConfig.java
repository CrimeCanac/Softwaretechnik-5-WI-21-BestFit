package com.example.demo.security;

// Author: Delbrin Alazo
// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: Configuration for Spring Security

import com.example.demo.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder für sichere Passwort-Hashing wird im
        // MitgliedDetailsService implementiert
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow CSS Files
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/**/*.css")).permitAll());

        // Allow pictures
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll());

        // Allow SVC Icons
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll());

        // Vaadin-Integration and Login-View
        setLoginView(http, LoginView.class);

        http.formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll());

        // overwrite default config, to avoid errors.
        super.configure(http);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Vaadin-spezifische statische Ressourcen werden von Spring Security ignoriert.
        // Können sonst ggf. blockiert werden
        web.ignoring().requestMatchers(VaadinWebSecurity.getDefaultWebSecurityIgnoreMatcher());
        super.configure(web);
    }
}