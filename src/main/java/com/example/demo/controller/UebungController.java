package com.example.demo.controller;

import com.example.demo.model.entities.Uebung;
import com.example.demo.model.enums.Muskelgruppe;
import com.example.demo.service.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/uebungen")
public class UebungController {

    private final UebungService uebungService;

    @Autowired
    public UebungController(UebungService uebungService) {
        this.uebungService = uebungService;
    }

    // Neue Übung hinzufügen
    @PostMapping
    public ResponseEntity<Uebung> uebungHinzufuegen(@RequestBody Uebung uebung) {
        // Validierung: Prüfen, ob die Muskelgruppe angegeben ist
        if (uebung.getMuskelgruppe() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Uebung neueUebung = uebungService.uebungHinzufuegen(uebung);
        return ResponseEntity.ok(neueUebung);
    }

    // Alle Übungen abrufen
    @GetMapping
    public ResponseEntity<List<Uebung>> alleUebungenAbrufen() {
        List<Uebung> uebungen = uebungService.alleUebungenAbrufen();
        return ResponseEntity.ok(uebungen);
    }

    // Übungen nach Muskelgruppe filtern
    @GetMapping("/filter")
    public ResponseEntity<List<Uebung>> uebungenNachMuskelgruppe(@RequestParam Muskelgruppe muskelgruppe) {
        List<Uebung> gefilterteUebungen = uebungService.uebungenNachMuskelgruppe(muskelgruppe);
        return ResponseEntity.ok(gefilterteUebungen);
    }

    // Übung nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Uebung> uebungNachIdAbrufen(@PathVariable Long id) {
        return uebungService.uebungNachIdAbrufen(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Übung bearbeiten
    @PutMapping("/{id}")
    public ResponseEntity<Uebung> uebungBearbeiten(@PathVariable Long id, @RequestBody Uebung uebungDetails) {
        // Validierung: Prüfen, ob die Muskelgruppe angegeben ist
        if (uebungDetails.getMuskelgruppe() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            Uebung bearbeiteteUebung = uebungService.uebungBearbeiten(id, uebungDetails);
            return ResponseEntity.ok(bearbeiteteUebung);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Übung löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> uebungLoeschen(@PathVariable Long id) {
        uebungService.uebungLoeschen(id);
        return ResponseEntity.noContent().build();
    }
}