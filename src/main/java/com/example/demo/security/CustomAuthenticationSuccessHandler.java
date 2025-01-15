package com.example.demo.security;

// Author: Delbrin Alazo
// Created: 2025-15.01
// Modified by: Delbrin Alazo
// Description: Klasse für die erfolgreiche Authentifizierung und dann Weiterleitung ins Dashboard

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
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/admin-dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_MANAGER")) {
                redirectUrl = "/manager-dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_EMPLOYEE")) {
                redirectUrl = "/employee-dashboard";
                break;
            }
        }

        if (redirectUrl.isEmpty()) {
            throw new IllegalStateException();
        }

        response.sendRedirect(redirectUrl);
    }
}