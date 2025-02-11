package com.example.demo.service;

//Author: Ömer Yalcinkaya

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.entities.Training;
import com.example.demo.model.repositories.TrainingRepository;

//Verwaltung von Trainingseinheiten.
@Service
public class TrainingService {

    @Autowired
    private final TrainingRepository repository;

    // Konstruktor für die Initialisierung des Repositories
    public TrainingService (TrainingRepository repository) {
        this.repository = repository;
    }
    
    // Gibt alle gespeicherten Trainings zurück
    public List<Training> getAllTrainings() {
        return this.repository.findAll();
    }

    // Ruft ein Training anhand seiner ID ab
    public Training getTrainingById(long id) {
        return this.repository.findById(id).orElse(null);
    }

    // Speichert oder aktualisiert ein Training
    public void saveTraining(Training training) {
        this.repository.save(training);
    }
    
    // Löscht ein Training anhand der ID
    public void deleteTrainingById(long id) {
        this.repository.deleteById(id);
    }
    
    // Gibt alle Trainings eines bestimmten Trainingsplans zurück
    public Set<Training> getTrainingsByTrainingsplanId(long trainingsplanId) {
        return this.repository.findByTrainingsplanId(trainingsplanId);
    }
    
}
