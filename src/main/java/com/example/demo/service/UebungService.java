package com.example.demo.service;

import com.example.demo.model.entities.Uebung;
import com.example.demo.model.enums.Muskelgruppe;
import com.example.demo.model.repositories.UebungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UebungService {

    private final UebungRepository uebungRepository;

    public UebungService(UebungRepository uebungRepository) {
        this.uebungRepository = uebungRepository;
    }

    public Uebung uebungHinzufuegen(Uebung uebung) {
        return uebungRepository.save(uebung);
    }

    public List<Uebung> alleUebungenAbrufen() {
        return uebungRepository.findAll();
    }

    public Optional<Uebung> uebungNachIdAbrufen(Long id) {
        return uebungRepository.findById(id);
    }

    public List<Uebung> uebungenNachMuskelgruppe(Muskelgruppe muskelgruppe) {
        return uebungRepository.findByMuskelgruppe(muskelgruppe);
    }

    public Uebung uebungBearbeiten(Long id, Uebung uebungDetails) {
        Uebung uebung = uebungRepository.findById(id).orElseThrow(() -> new RuntimeException("Übung nicht gefunden."));
        uebung.setName(uebungDetails.getName());
        uebung.setBeschreibung(uebungDetails.getBeschreibung());
        uebung.setMuskelgruppe(uebungDetails.getMuskelgruppe());
        uebung.setGeraet(uebungDetails.getGeraet());
        return uebungRepository.save(uebung);
    }

    public void uebungLoeschen(Long id) {
        uebungRepository.deleteById(id);
    }
}
