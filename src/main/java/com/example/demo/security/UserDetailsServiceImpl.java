package com.example.demo.security;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2025-01-31
// Modified by: Delbrin Alazo
// Description: Class for handling user details

import com.example.demo.model.entities.User;
import com.example.demo.model.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            List<GrantedAuthority> authorities = getAuthorities(user);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRolle().toUpperCase()));
            return new org.springframework.security.core.userdetails.User(user.getBenutzername(), user.getPasswort(),
                    authorities);
        }
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
