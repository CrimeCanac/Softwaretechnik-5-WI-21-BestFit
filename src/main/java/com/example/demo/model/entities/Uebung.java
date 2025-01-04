package com.example.demo.model.entities;

import java.util.Objects;

import com.example.demo.model.enums.Muskelgruppe;

import jakarta.persistence.*;

@Entity
public class Uebung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String beschreibung;

    @ManyToOne
    @JoinColumn(name = "geraet_id")
    private Geraet geraet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Muskelgruppe muskelgruppe;

    // Konstruktoren
    public Uebung() {}

    public Uebung(String name, String beschreibung, Geraet geraet, Muskelgruppe muskelgruppe) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.geraet = geraet;
        this.muskelgruppe = muskelgruppe;
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

    public Geraet getGeraet() {
        return geraet;
    }

    public void setGeraet(Geraet geraet) {
        this.geraet = geraet;
    }

    public Muskelgruppe getMuskelgruppe() {
        return muskelgruppe;
    }

    public void setMuskelgruppe(Muskelgruppe muskelgruppe) {
        this.muskelgruppe = muskelgruppe;
    }
}
