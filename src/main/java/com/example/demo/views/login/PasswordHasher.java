package com.example.demo.views.login;
// Author: Delbrin Alazo

// Created: 2024-12-08
// Last Updated: 2024-12-08
// Modified by: Delbrin Alazo
// Description: Class for storing hashing passwords

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "yV@)A~=*3aT3dF|~>KY2";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Hashed password: " + encodedPassword);
    }
}

/*
 * Master
 * yV@)A~=*3aT3dF|~>KY2
 * Answer: Not existent
 * 
 * Owner
 * Hallo1234!
 * Hallo1234!
 * 
 * Mitarbeiter
 * Hallo1234!
 * Hallo1234!
 * 
 * Username: MaxMustermann
 * Password: Hallo1234!
 * SecurityQuestionAnswer: Musterstadt
 */