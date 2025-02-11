package com.example.demo.views.uebung;

// Author: Ömer Yalcinkaya

import com.example.demo.model.entities.Uebung;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;


// UI-Komponente zur Darstellung eines Übungssatzes innerhalb eines Trainings
public class UebungComponent extends HorizontalLayout{

    private IntegerField ifWdh = new IntegerField();    // Eingabefeld für Wiederholungen
    private NumberField nfGewicht = new NumberField();  // Eingabefeld für Gewicht
    private TextField tfSatz = new TextField();         // Anzeige für Satznummer
    private Button btnDelete = new Button("", VaadinIcon.TRASH.create());   // Löschen-Button

    public UebungComponent(UebungLayout parent, Uebung uebung, int satznr) {
        
        // Initialisierung der UI-Komponenten
        tfSatz.setValue(satznr + ".");
        tfSatz.setLabel("Satz");
        tfSatz.setReadOnly(true);
        tfSatz.setWidth("60px");

        ifWdh.setLabel("Wdh");
        ifWdh.setMin(0);
        ifWdh.setMax(100);
        ifWdh.setWidth("100px");

        nfGewicht.setLabel("Gewicht");
        nfGewicht.setMin(0);
        nfGewicht.setMax(1000);
        nfGewicht.setWidth("100px");

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> {
            parent.deleteSatz(tfSatz.getValue());
        });

        // Layout-Ausrichtung
        setAlignItems(Alignment.BASELINE);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("100%");

        // UI-Komponenten hinzufügen
        add(tfSatz, ifWdh, nfGewicht, btnDelete);
    }
    
    // Setzt den Wert für Wiederholungen
    public void setIfWdhValue(int wdh) {
        ifWdh.setValue(wdh);
    }

    // Gibt den aktuellen Wert der Wiederholungen zurück
    public int getIfWdhValue() {
        if(ifWdh.getValue() == null) {
            return 0;
        } else {
            return ifWdh.getValue();
        }
    }

    // Setzt den Wert für das Gewicht
    public void setNfGewichtValue(double gewicht) {
        nfGewicht.setValue(gewicht);
    }

    // Gibt den aktuellen Wert des Gewichts zurück
    public double getNfGewichtValue() {
        if(nfGewicht.getValue() == null) {
            return 0;
        } else {
            return nfGewicht.getValue();
        }
    }

    // Setzt die Satznummer
    public void setTfSatzValue(int satznr) {
        tfSatz.setValue(satznr + ".");
    }

    // Gibt die Satznummer als String zurück
    public String getTfSatzValue_String() {
        return tfSatz.getValue();
    }

    // Gibt die Satznummer als Integer zurück
    public int getTfSatzValue_int() {
        String value = tfSatz.getValue();
        if (value.matches("\\d+\\.?")) {
            int satz = Integer.parseInt(value.replace(".", ""));
            if (satz > 0) {
                return satz;
            } else {
                return 0;
            }

        } else {
            throw new NumberFormatException("Ungültiger Wert: " + value);
        }
    }
}
