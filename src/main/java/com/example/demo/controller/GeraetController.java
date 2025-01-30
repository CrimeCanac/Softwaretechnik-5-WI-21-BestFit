package com.example.demo.controller;

//Author: Fabian Müller

import com.example.demo.model.entities.Geraet;
import com.example.demo.model.entities.Uebung;
import com.example.demo.service.GeraetService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/geraete")
public class GeraetController {

    private final GeraetService geraetService;

    @Autowired
    public GeraetController(GeraetService geraetService) {
        this.geraetService = geraetService;
    }

    // Neues Gerät hinzufügen
    @PostMapping
    public ResponseEntity<Geraet> geraetHinzufuegen(@RequestBody Geraet geraet) {
        Geraet neuesGeraet = geraetService.geraetHinzufuegen(geraet);
        return ResponseEntity.ok(neuesGeraet);
    }

    // Alle Geräte abrufen
    @GetMapping
    public ResponseEntity<List<Geraet>> alleGeraeteAbrufen() {
        List<Geraet> geraete = geraetService.alleGeraeteAbrufen();
        return ResponseEntity.ok(geraete);
    }
    
 // Premium-Geräte abrufen
    @GetMapping("/premium")
    public ResponseEntity<List<Geraet>> allePremiumGeraeteAbrufen() {
        List<Geraet> premiumGeraete = geraetService.allePremiumGeraeteAbrufen();
        return ResponseEntity.ok(premiumGeraete);
    }

    // Gerät nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Geraet> geraetNachIdAbrufen(@PathVariable Long id) {
        return geraetService.geraetNachIdAbrufen(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Gerät bearbeiten
    @PutMapping("/{id}")
    public ResponseEntity<Geraet> geraetBearbeiten(@PathVariable Long id, @RequestBody Geraet geraetDetails) {
        try {
            Geraet bearbeitetesGeraet = geraetService.geraetBearbeiten(id, geraetDetails);
            return ResponseEntity.ok(bearbeitetesGeraet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Gerät löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> geraetLoeschen(@PathVariable Long id) {
        try {
            geraetService.geraetLoeschen(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
 // Test-Methode für das Thymeleaf-Template
    @GetMapping("/test")
    public String testTemplate(Model model) {
        List<Geraet> geraete = geraetService.alleGeraeteAbrufen();
        model.addAttribute("geraete", geraete);
        return "geraeteliste";
    }
    
   

    
}
