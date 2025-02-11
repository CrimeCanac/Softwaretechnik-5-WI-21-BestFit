package com.example.demo.service;  

//Author: Ömer Yalcinkaya

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.entities.Trainingshistorie;
import com.example.demo.model.repositories.TrainingshistorieRepository;

// Verwaltung der Trainingshistorie.
@Service
public class TrainingshistorieService {

    @Autowired
    private final TrainingshistorieRepository repository;
    
    // Konstruktor zur Initialisierung des Repositories
    public TrainingshistorieService (TrainingshistorieRepository repository) {
        this.repository = repository;
    }
    
    // Gibt alle gespeicherten Trainingshistorien zurück
    public List<Trainingshistorie> getAllTrainingshistorie() {
        return this.repository.findAll();
    }
    
    // Ruft die Trainingshistorie eines bestimmten Benutzers anhand der User-ID ab
    public List<Trainingshistorie> findByUserId(Long userID) {
        return this.repository.findByUserId(userID);
    }

    // Ruft eine Trainingshistorie anhand der ID ab
    public Trainingshistorie getTrainingshistorieById(long id) {
        return this.repository.findById(id).orElse(null);
    }

    // Speichert oder aktualisiert eine Trainingshistorie
    public void saveTrainingshistorie(Trainingshistorie training) {
        this.repository.save(training);
    }

    // Löscht eine Trainingshistorie anhand der ID
    public void deleteTrainingshistorieById(long id) {
        this.repository.deleteById(id);
    }
    
}