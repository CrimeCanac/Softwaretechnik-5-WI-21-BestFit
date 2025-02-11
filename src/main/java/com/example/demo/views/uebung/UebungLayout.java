package com.example.demo.views.uebung;

// Author: Ömer Yalcinkaya

import java.util.*;

import com.example.demo.model.entities.Uebung;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

// UI-Komponente zur Verwaltung einer Übung mit mehreren Sätzen
public class UebungLayout extends VerticalLayout {

    private Uebung uebung;
    private List<UebungComponent> uebungComponents = new ArrayList<UebungComponent>();  // Liste der Übungskomponenten (Sätze)
    private HorizontalLayout hlHeader = new HorizontalLayout();                         // Kopfzeile mit Übungsname und Hinzufügen-Button
    private VerticalLayout vlContent = new VerticalLayout();                            // Bereich für die Sätze
    private Button btnAddSatz = new Button(VaadinIcon.PLUS.create());                   // Button zum Hinzufügen eines Satzes

    public UebungLayout(Uebung uebung, int anzSaetze) {
        this.uebung = uebung;

        // Kopfzeile mit Übungsname und Hinzufügen-Button erstellen
        hlHeader.add(new H4(uebung.getName()), btnAddSatz);

        // Initiale Sätze erstellen
        for (int i = 1; i <= anzSaetze; i++) {
            uebungComponents.add(new UebungComponent(this, uebung, i));
        }

        // Sätze zur UI hinzufügen
        for (HorizontalLayout x : uebungComponents) {
            vlContent.add(x);
        }

        // Das Hinzufügen eines neuen Satzes
        btnAddSatz.addClickListener(e -> {
            int satznr = uebungComponents.size() + 1;

            if (satznr > 10) {
                Notification.show("Maximal 10 Sätze erlaubt", 3000, Notification.Position.MIDDLE);
                return;
            } else if (satznr == 1) {
                vlContent.setVisible(true);
            }

            // Neuen Satz erstellen und zur UI hinzufügen
            UebungComponent satz = new UebungComponent(this, uebung, satznr);
            uebungComponents.add(satz);
            vlContent.add(satz);
            setVisible(true);
        });
        
        // Layout-Ausrichtung und UI-Struktur
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("100%");
        hlHeader.setAlignItems(Alignment.BASELINE);
        add(hlHeader, vlContent);
    }

    // Entfernt einen Satz anhand der Satznummer
    public void deleteSatz(String strSatz) {
        for (UebungComponent x : uebungComponents) {

            if (strSatz.equals(x.getTfSatzValue_String())) {
                uebungComponents.remove(x);
                vlContent.remove(x);

                if (uebungComponents.size() == 0) {
                    vlContent.setVisible(false);
                }

                resetSatznr();   // Satznummern neu durchnummerieren
                return;
            }
        }
    }

    // Aktualisiert die Satznummern nach einer Änderung
    private void resetSatznr() {
        int satznr = 1;
        for (UebungComponent x : uebungComponents) {
            x.setTfSatzValue(satznr);
            satznr++;
        }
    }

    // Gibt die Gesamtanzahl der Sätze zurück
    public int getSumSaetze() {
        return uebungComponents.size();
    }

    // Berechnet die Gesamtanzahl der Wiederholungen aller Sätze
    public int getSumWdh() {
        int sum = 0;
        for (UebungComponent x : uebungComponents) {
            sum += x.getIfWdhValue();
        }
        return sum;
    }

    // Berechnet das Gesamtgewicht aller Sätze
    public double getSumGewicht() {
        int sum = 0;
        for (UebungComponent x : uebungComponents) {
            sum += x.getNfGewichtValue();
        }
        return sum;
    }
}
