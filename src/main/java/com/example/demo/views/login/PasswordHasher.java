package com.example.demo.views.login;
// Author: Delbrin Alazo

// Created: 2024-12-08
// Last Updated: 2024-12-08
// Modified by: Delbrin Alazo
// Description: Klasse zum Hashen von Passwörtern

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "yV@)A~=*3aT3dF|~>KY2";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Gehashtes Passwort: " + encodedPassword);
    }
}

/*
 * Benutzername: MaxMustermann
 * Passwort: Hallo1234!
 * SicherheitsfrageAntwort: Musterstadt
 * 
 * Master
 * yV@)A~=*3aT3dF|~>KY2
 * Antwort
 */