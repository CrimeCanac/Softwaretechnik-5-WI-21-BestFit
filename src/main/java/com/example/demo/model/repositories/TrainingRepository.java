package com.example.demo.model.repositories;

//Author: Ömer Yalcinkaya

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entities.Training;

// Verwaltung von Trainings-Entitäten
@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>{
    
    // Abfrage zur Suche aller Trainings, die zu einem bestimmten Trainingsplan gehören
    @Query("SELECT t FROM Training t JOIN t.trainingsplaene tp WHERE tp.id = :trainingsplanId")
    Set<Training> findByTrainingsplanId(@Param("trainingsplanId") long trainingsplanId);
    
    

}
