package com.example.demo.security;

// Author: Delbrin Alazo
// Created: 2025-15.01
// Modified by: Delbrin Alazo
// Description: Class for handling the authentication success and redirecting the user to the appropriate dashboard

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "";

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_MITGLIED")) {
                redirectUrl = "/mitglied-dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_ADMINISTRATOR")) {
                redirectUrl = "/admin-dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_GESCHAEFTSFUEHRER")) {
                redirectUrl = "/geschaeftsfuehrer-dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_MITARBEITER")) {
                redirectUrl = "/mitarbeiter-dashboard";
                break;
            }
        }

        if (redirectUrl.isEmpty()) {
            throw new IllegalStateException();
        }

        response.sendRedirect(redirectUrl);
    }
}