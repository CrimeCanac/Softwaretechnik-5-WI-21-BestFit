package com.example.demo.model.repositories;

//Author: Ömer Yalcinkaya

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entities.Trainingshistorie;
import com.example.demo.model.entities.Trainingsplan;

// Verwaltung der Trainingshistorie-Entitäten
@Repository
public interface TrainingshistorieRepository extends JpaRepository<Trainingshistorie, Long>{

    // Abfrage zur Suche aller Trainingshistorien eines bestimmten Benutzers
    @Query("SELECT t FROM Trainingshistorie t WHERE t.user.id = :userID")
    List<Trainingshistorie> findByUserId(@Param("userID") Long userID);
     

    
}

