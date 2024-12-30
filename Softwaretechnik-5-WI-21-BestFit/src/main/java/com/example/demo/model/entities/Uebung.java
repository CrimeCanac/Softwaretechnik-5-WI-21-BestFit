package com.example.demo.model.entities;

import java.util.Objects;

import com.example.demo.model.enums.Muskelgruppe;

import jakarta.persistence.*;

@Entity
public class Uebung {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uebung_seq")
    @SequenceGenerator(name = "uebung_seq", sequenceName = "uebung_seq", initialValue = 50)
    private long id;
    @Column(nullable = false)
    private String name;
    private String beschreibung;
    @Enumerated(EnumType.STRING)
    private Muskelgruppe muskelgruppe;

    public Uebung() {
    }

    public Uebung(String name, Muskelgruppe muskelgruppe, String beschreibung) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.muskelgruppe = muskelgruppe;
    }

    public Uebung(String name, Muskelgruppe muskelgruppe ) {
        this.name = name;
        this.muskelgruppe = muskelgruppe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Muskelgruppe getMuskelgruppe() {
        return muskelgruppe;
    }

    public void setMuskelgruppe(Muskelgruppe muskelgruppe) {
        this.muskelgruppe = muskelgruppe;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Uebung other = (Uebung) obj;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}