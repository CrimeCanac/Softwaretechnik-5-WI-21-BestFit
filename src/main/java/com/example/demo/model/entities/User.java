package com.example.demo.model.entities;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: Entity for User

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

import com.example.demo.model.enums.Role;
import com.example.demo.model.enums.Sicherheitsfrage;

@Entity
@Table(name = "application_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String benutzername;

    @Column(nullable = false)
    private String passwort;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    @Column(nullable = false)
    private String sicherheitsfrageAntwort;

    @Enumerated(EnumType.STRING)
    private Sicherheitsfrage sicherheitsfrage;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(nullable = false)
    private String rolle;

    @Column
    private Double groesseCm;

    @Column
    private Double gewichtKg;

    // Getter and Setter

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getSicherheitsfrageAntwort() {
        return sicherheitsfrageAntwort;
    }

    public void setSicherheitsfrageAntwort(String sicherheitsfrageAntwort) {
        this.sicherheitsfrageAntwort = sicherheitsfrageAntwort;
    }

    public Sicherheitsfrage getSicherheitsfrage() {
        return sicherheitsfrage;
    }

    public void setSicherheitsfrage(Sicherheitsfrage sicherheitsfrage) {
        this.sicherheitsfrage = sicherheitsfrage;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Double getGroesseCm() {
        return groesseCm;
    }

    public void setGroesseCm(Double groesseCm) {
        this.groesseCm = groesseCm;
    }

    public Double getGewichtKg() {
        return gewichtKg;
    }

    public void setGewichtKg(Double gewichtKg) {
        this.gewichtKg = gewichtKg;
    }
}