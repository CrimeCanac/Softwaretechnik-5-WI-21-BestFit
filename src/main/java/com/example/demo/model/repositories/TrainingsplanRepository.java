package com.example.demo.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entities.Trainingsplan;
import com.example.demo.model.entities.Uebung;

@Repository
public interface TrainingsplanRepository extends JpaRepository<Trainingsplan, Long> {

    @Query("SELECT t FROM Trainingsplan t WHERE t.name LIKE %:name_str%")
    List<Trainingsplan> filterTrainingsplanByName(@Param("name_str") String nameStr);
    
  
}
