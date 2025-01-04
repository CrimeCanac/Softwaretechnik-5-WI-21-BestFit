package com.example.demo.model.repositories;

import com.example.demo.model.enums.Muskelgruppe;
import com.example.demo.model.entities.Uebung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UebungRepository extends JpaRepository<Uebung, Long> {
    List<Uebung> findByMuskelgruppe(Muskelgruppe muskelgruppe);
}
