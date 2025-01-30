package com.example.demo.model.repositories;

//Author: Fabian Müller

import com.example.demo.model.entities.Geraet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeraetRepository extends JpaRepository<Geraet, Long> {
	List<Geraet> findByIsPremium(boolean isPremium);// Zusätzliche Abfragen können hier definiert werden, falls nötig
}