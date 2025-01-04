package com.example.demo.model.entities;

import jakarta.persistence.*;

@Entity
public class Geraet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String beschreibung;
    
    @Column(nullable = false)
    private boolean isPremium;

    // Konstruktoren
    public Geraet() {}

    public Geraet(String name, String beschreibung, boolean isPremium) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.isPremium = isPremium;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
    
    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
}

