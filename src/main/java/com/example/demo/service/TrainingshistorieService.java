package com.example.demo.service;    
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.entities.Trainingshistorie;
import com.example.demo.model.repositories.TrainingshistorieRepository;

@Service
public class TrainingshistorieService {

    @Autowired
    private final TrainingshistorieRepository repository;

    public TrainingshistorieService (TrainingshistorieRepository repository) {
        this.repository = repository;
    }

    public List<Trainingshistorie> getAllTrainingshistorie() {
        return this.repository.findAll();
    }

    public List<Trainingshistorie> findByUserId(Long userID) {
        return this.repository.findByUserId(userID);
    }

    public Trainingshistorie getTrainingshistorieById(long id) {
        return this.repository.findById(id).orElse(null);
    }

    public void saveTrainingshistorie(Trainingshistorie training) {
        this.repository.save(training);
    }

    public void deleteTrainingshistorieById(long id) {
        this.repository.deleteById(id);
    }
    
}