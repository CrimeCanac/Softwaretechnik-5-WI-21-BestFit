package com.example.demo.model.entities;

//Author: Ömer Yalcinkaya

import java.time.*;

import jakarta.persistence.*;
@Entity
public class Trainingshistorie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "training_seq", initialValue = 100)
    private long id;

    private LocalDateTime datum;
    private int dauer_sek;
    private LocalTime dauer_zeit;
    private String dauer_string;

    private int sumSaetze;
    private int sumWdh;
    private double sumGewicht;
    private int sumUebungen;

    // Beziehung zum Training (Many-to-One)
    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Training training;

    // Beziehung zum Benutzer (Many-to-One)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private User user;

    // Konstruktor setzt aktuelles Datum
    public Trainingshistorie() {
        this.datum = LocalDateTime.now();
    }

    // Getter & Setter
    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public int getDauer_sek() {
        return dauer_sek;
    }

    public void setDauer_sek(int dauer_sek) {
        this.dauer_sek = dauer_sek;
    }

    public LocalTime getDauer_zeit() {
        return dauer_zeit;
    }

    public void setDauer_zeit(LocalTime dauer_zeit) {
        this.dauer_zeit = dauer_zeit;
    }

    public String getDauer_string() {
        return dauer_string;
    }

    public void setDauer_string(String dauer_string) {
        this.dauer_string = dauer_string;
    }
    
    //Setzt Dauer, konvertiert String (Format: HH:mm:ss)
    public void setDauerByString(String dauer_string) {
        String[] parts = dauer_string.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        this.dauer_zeit = LocalTime.of(hours, minutes, seconds);
        this.dauer_sek = hours * 3600 + minutes * 60 + seconds;
        this.dauer_string = dauer_string;
        System.out.println("Dauer: " + dauer_string);
        System.out.println("Dauer: " + dauer_zeit);
        System.out.println("Dauer: " + dauer_sek);
    }

    public int getSumSaetze() {
        return sumSaetze;
    }

    public void setSumSaetze(int sumSaetze) {
        this.sumSaetze = sumSaetze;
    }

    public int getSumWdh() {
        return sumWdh;
    }

    public void setSumWdh(int sumWdh) {
        this.sumWdh = sumWdh;
    }

    public double getSumGewicht() {
        return sumGewicht;
    }

    //Durchschnittliche Gewicht pro Wiederholung
    public int getAvgGewicht() {
        if(sumWdh == 0) {
            return (int)sumGewicht / 1;
        } 
        return (int)sumGewicht / sumWdh;
    }

    public void setSumGewicht(double sumGewicht) {
        this.sumGewicht = sumGewicht;
    }

    public int getSumUebungen() {
        return sumUebungen;
    }

    public void setSumUebungen(int sumUebungen) {
        this.sumUebungen = sumUebungen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}


