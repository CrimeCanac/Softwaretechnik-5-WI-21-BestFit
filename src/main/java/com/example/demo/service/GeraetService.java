package com.example.demo.service;

//Author: Fabian Müller

import com.example.demo.model.entities.Geraet;
import com.example.demo.model.entities.Uebung;
import com.example.demo.model.repositories.GeraetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeraetService {

    private final GeraetRepository geraetRepository;

    @Autowired
    public GeraetService(GeraetRepository geraetRepository) {
        this.geraetRepository = geraetRepository;
    }

    // Gerät hinzufügen
    public Geraet geraetHinzufuegen(Geraet geraet) {
        return geraetRepository.save(geraet);
    }

    // Alle Geräte abrufen
    public List<Geraet> alleGeraeteAbrufen() {
        return geraetRepository.findAll();
    }
    
 // Premium-Geräte abrufen
    public List<Geraet> allePremiumGeraeteAbrufen() {
        return geraetRepository.findByIsPremium(true);
    }
    
//  // Basis-Geräte abrufen
    public List<Geraet> alleBasisGeraeteAbrufen() {
        return geraetRepository.findByIsPremium(false);
    }
        
    
    // Gerät nach ID abrufen
    public Optional<Geraet> geraetNachIdAbrufen(Long id) {
        return geraetRepository.findById(id);
    }

    // Gerät bearbeiten
    public Geraet geraetBearbeiten(Long id, Geraet geraetDetails) {
        Optional<Geraet> optionalGeraet = geraetRepository.findById(id);
        if (optionalGeraet.isPresent()) {
            Geraet geraet = optionalGeraet.get();
            geraet.setName(geraetDetails.getName());
            geraet.setBeschreibung(geraetDetails.getBeschreibung());
            geraet.setPremium(geraetDetails.isPremium());
            return geraetRepository.save(geraet);
        }
        throw new RuntimeException("Geraet mit ID " + id + " nicht gefunden.");
    }

    // Gerät löschen
    public void geraetLoeschen(Long id) {
    	try {
        geraetRepository.deleteById(id);
    	} catch (DataIntegrityViolationException ex) {
            // Fehler abfangen und benutzerdefinierte Ausnahme werfen
            throw new RuntimeException("Gerät / Bereich wird in einer oder mehreren Übungen verwendet und kann nicht gelöscht werden.");
        }
    }
}
