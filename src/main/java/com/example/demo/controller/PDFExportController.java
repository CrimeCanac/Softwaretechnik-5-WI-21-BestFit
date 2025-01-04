package com.example.demo.controller;

import com.example.demo.model.entities.Geraet;
import com.example.demo.model.entities.Trainingsplan;
import com.example.demo.model.entities.Uebung;
import com.example.demo.service.GeraetService;
import com.example.demo.service.TrainingsplanService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
public class PDFExportController {

    private final TrainingsplanService trainingsplanService;
    private final GeraetService geraetService;

    @Autowired
    public PDFExportController(TrainingsplanService trainingsplanService, GeraetService geraetService) {
        this.trainingsplanService = trainingsplanService;
        this.geraetService = geraetService;
    }

    // Export für Trainingsplan
    @GetMapping("/trainingsplan/{id}/uebungen")
    public void exportTrainingsplanUebungenAsPDF(@PathVariable Long id, HttpServletResponse response) throws Exception {
        // Trainingsplan abrufen
        Trainingsplan trainingsplan = trainingsplanService.findTrainingsplanById(id);
        if (trainingsplan == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Trainingsplan nicht gefunden.");
            return;
        }

        // Alle Übungen aus den Trainings des Trainingsplans sammeln
        List<Uebung> uebungen = trainingsplan.getTrainings()
                                             .stream()
                                             .flatMap(training -> training.getUebungen().stream())
                                             .distinct()
                                             .toList();

        // Thymeleaf Template rendern
        String htmlContent = ThymeleafTemplateRenderer.render("trainingsplan", Map.of("uebungen", uebungen));

        // PDF-Header setzen
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=uebungen_im_trainingsplan.pdf");

        // PDF generieren
        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Fehler beim Generieren der PDF.");
        }
    }

    // Export für Geräte-Liste
    @GetMapping("/geraete/pdf")
    public void exportGeraetelisteAsPDF(HttpServletResponse response) throws Exception {
        // Geräte abrufen
        List<Geraet> geraete = geraetService.alleGeraeteAbrufen();

        // Thymeleaf Template rendern
        String htmlContent = ThymeleafTemplateRenderer.render("geraeteliste", Map.of("geraete", geraete));

        // PDF-Header setzen
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=geraeteliste.pdf");

        // PDF generieren
        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Fehler beim Generieren der PDF.");
        }
    }
}
