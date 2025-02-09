package com.example.demo.service;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2025-01-31
// Modified by: Delbrin Alazo
// Description: Class for User Service 

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.entities.User;
import com.example.demo.model.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public boolean userExists(String username) {
        User user = repository.findByUsername(username);
        return user != null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<User> list() {
        return repository.findAll();
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User getCurrentUser() {
        User user;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            user = findByUsername(((UserDetails) principal).getUsername());
            System.out.println("User: " + user.getBenutzername());
            return user;
        } else {
            return null;
        }
    }

    public boolean verifyMasterPassword(String masterPassword) {
        User masterUser = repository.findByUsername("Master");
        if (masterUser != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(masterPassword, masterUser.getPasswort());
        }
        return false;
    }

    public boolean verifyAdminOrGeschaeftsfuehrerPassword(String password) {
        List<User> adminsAndGeschaeftsfuehrer = repository.findAll().stream()
                .filter(user -> user.getRolle().equals("administrator") || user.getRolle().equals("geschaeftsfuehrer"))
                .collect(Collectors.toList());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (User user : adminsAndGeschaeftsfuehrer) {
            if (encoder.matches(password, user.getPasswort())) {
                return true;
            }
        }
        return false;
    }

    public List<User> findAllGeschaeftsfuehrer() {
        return repository.findAllGeschaeftsfuehrer();
    }

    public List<User> findAllMitarbeiter() {
        return repository.findAllMitarbeiter();
    }

    public List<User> findAllMitglieder() {
        return repository.findAllMitglieder();
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}