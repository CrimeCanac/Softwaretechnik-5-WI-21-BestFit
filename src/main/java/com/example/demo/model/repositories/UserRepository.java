package com.example.demo.model.repositories;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2025-01-31
// Modified by: Delbrin Alazo
// Description: Repository for User 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entities.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE u.benutzername = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.rolle = 'mitarbeiter'")
    List<User> findAllMitarbeiter();

    @Query("SELECT u FROM User u WHERE u.rolle = 'mitglied'")
    List<User> findAllMitglieder();

    @Query("SELECT u FROM User u WHERE u.rolle = 'geschaeftsfuehrer'")
    List<User> findAllGeschaeftsfuehrer();
}