package com.example.demo.model.entities;

//Author: Ömer Yalcinkaya

import java.util.*;

import jakarta.persistence.*;


@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "training_seq", initialValue = 100)
    private long id;
    @Column(nullable = false)
    private String name;
    private String beschreibung;

    // Beziehung zu Trainingsplänen (Many-to-Many)
    @ManyToMany(mappedBy = "trainings", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Trainingsplan> trainingsplaene;

    // Beziehung zu Übungen (Many-to-Many)
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "training_uebungen",
        joinColumns = @JoinColumn(name = "training_id"),
        inverseJoinColumns = @JoinColumn(name = "uebung_id"))
    private List<Uebung> uebungen;

    // Beziehung zur Trainingshistorie (One-to-Many)
    @OneToMany(mappedBy = "training", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) 
    private Set<Trainingshistorie> trainingHistorie;

    // Standardkonstruktor zur Initialisierung der Collections
    public Training () {
        this.trainingsplaene = new HashSet<>();
        this.uebungen = new ArrayList<>();
        this.trainingHistorie = new HashSet<>();
    }

    // Getter & Setter
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
    
    public List<Uebung> getUebungen() {
        return uebungen;
    }

    public void setUebungen(List<Uebung> uebungen) {
        this.uebungen = uebungen;
    }
    
    // Methoden zum Hinzufügen und Entfernen von Übungen
    public void addUebung(Uebung uebung) {
        this.uebungen.add(uebung);
    }

    public void removeUebung(Uebung uebung) {
        if (this.uebungen.contains(uebung)) {
            this.uebungen.remove(uebung);
        } 
    }

    public Set<Trainingshistorie> getTrainingHistorie() {
        return trainingHistorie;
    }

    public void setTrainingHistorie(Set<Trainingshistorie> trainingHistorie) {
        this.trainingHistorie = trainingHistorie;
    }
    
    // Methoden zum Hinzufügen und Entfernen von Trainingshistorie-Einträgen
    public void addTrainingHistorie(Trainingshistorie trainingshistorie) {
        this.trainingHistorie.add(trainingshistorie);
    }

    public void removeTrainingHistorie(Trainingshistorie trainingshistorie) {
        if (this.trainingHistorie.contains(trainingshistorie)) {
            this.trainingHistorie.remove(trainingshistorie);
        }
    }

    public Set<Trainingsplan> getTrainingsplaene() {
        return trainingsplaene;
    }

    public void setTrainingsplaene(Set<Trainingsplan> trainingsplaene) {
        this.trainingsplaene = trainingsplaene;
    }
    
    // Methoden zum Hinzufügen und Entfernen von Trainingsplänen
    public void addTrainingsplan(Trainingsplan trainingsplan) {
        this.trainingsplaene.add(trainingsplan);
    }

    public void removeTrainingsplan(Trainingsplan trainingsplan) {
        if (this.trainingsplaene.contains(trainingsplan)) {
            this.trainingsplaene.remove(trainingsplan);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
