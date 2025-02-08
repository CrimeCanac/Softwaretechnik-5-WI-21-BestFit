package com.example.demo.service;

//Author: Ömer Yalcinkaya

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.entities.Training;
import com.example.demo.model.repositories.TrainingRepository;

@Service
public class TrainingService {

    @Autowired
    private final TrainingRepository repository;

    public TrainingService (TrainingRepository repository) {
        this.repository = repository;
    }

    public List<Training> getAllTrainings() {
        return this.repository.findAll();
    }

    public Training getTrainingById(long id) {
        return this.repository.findById(id).orElse(null);
    }

    public void saveTraining(Training training) {
        this.repository.save(training);
    }

    public void deleteTrainingById(long id) {
        this.repository.deleteById(id);
    }

    public Set<Training> getTrainingsByTrainingsplanId(long trainingsplanId) {
        return this.repository.findByTrainingsplanId(trainingsplanId);
    }
    
}
