package com.example.demo.model.entities;

import java.util.*;

import jakarta.persistence.*;
@Entity
public class Trainingsplan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "trainingsplan_seq")
    @SequenceGenerator(name = "trainingsplan_seq", sequenceName = "trainingsplan_seq", initialValue = 50)
    private long id;
    private String name;
    private String beschreibung;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "benutzer_id")
    private User benutzer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "trainingsplan_trainings",
            joinColumns = @JoinColumn(name = "trainingsplan_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private Set<Training> trainings;

    public Trainingsplan() {
        this.trainings = new HashSet<>();
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

    public User getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(User benutzer) {
        this.benutzer = benutzer;
    }

    public Set<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(Set<Training> trainings) {
        this.trainings = trainings;
    }

    public void addTraining(Training training) {
        try {
            this.trainings.add(training);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTraining(Training training) {
        try {
            this.trainings.remove(training);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    
}

