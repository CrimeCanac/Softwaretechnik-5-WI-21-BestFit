package com.example.demo.views.Geraete;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

import com.example.demo.views.ManagerLayout;
import com.example.demo.model.entities.Geraet;
import com.example.demo.service.GeraetService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.FlexComponent;


import java.util.List;

@PageTitle("Geräteverwaltung")
@Route(value = "manager/geraete", layout = ManagerLayout.class)
@PermitAll
public class ManagerMachinesView extends VerticalLayout {

    private final GeraetService geraetService;
    private Grid<Geraet> geraetGrid;

    @Autowired
    public ManagerMachinesView(GeraetService geraetService) {
        this.geraetService = geraetService;

        setSpacing(true);
        setPadding(true);

     // Button: Neues Gerät hinzufügen
        Button addGeraetButton = new Button("Gerät / Bereich hinzufügen", event -> openAddGeraetDialog());
        addGeraetButton.getStyle().set("background-color", "#28a745");
        addGeraetButton.getStyle().set("color", "white");
        
     // PDF-Export-Button
        Button exportPdfButton = new Button("PDF Export");
        exportPdfButton.getElement().setProperty("innerHTML", "<i class='fa fa-file-pdf-o' style='color: #007BFF; margin-right: 8px;'></i>PDF Export");
        exportPdfButton.getStyle().set("background-color", "#007BFF"); // Blaue Hintergrundfarbe
        exportPdfButton.getStyle().set("color", "white");
        exportPdfButton.getStyle().set("border", "none");
        exportPdfButton.getStyle().set("padding", "10px 15px");
        exportPdfButton.getStyle().set("border-radius", "5px");
        exportPdfButton.getStyle().set("font-size", "16px");

        // Click-Listener für den Button
        exportPdfButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.getPage().open("/geraete/pdf"));
        });

        // Tooltip für den Button
        exportPdfButton.getElement().setAttribute("title", "Geräteliste als PDF exportieren");

       

        HorizontalLayout headerLayout = new HorizontalLayout(exportPdfButton,addGeraetButton);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.END);

        // Grid: Geräteliste
        geraetGrid = new Grid<>(Geraet.class, false);
        geraetGrid.addColumn(Geraet::getId).setHeader("ID").setWidth("100px").setFlexGrow(0);
        geraetGrid.addColumn(Geraet::getName).setHeader("Name");
        geraetGrid.addColumn(Geraet::getBeschreibung).setHeader("Beschreibung");
        geraetGrid.addColumn(uebung -> uebung.isPremium() ? "Ja" : "Nein")
        .setHeader("Premium");
        geraetGrid.addComponentColumn(geraet -> createActions(geraet)).setHeader("Aktionen");

        // Geräte aus der Datenbank laden
        updateGrid();

        add(headerLayout, geraetGrid);
    }

    private void updateGrid() {
        List<Geraet> geraete = geraetService.alleGeraeteAbrufen();
        geraetGrid.setItems(geraete);
    }

    private void openAddGeraetDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Gerät / Bereich hinzufügen");
        dialog.setWidth("400px");
        dialog.setHeight("auto");
        dialog.setResizable(true); // Dialog ist skalierbar

        // Name
        TextField nameField = new TextField("Name");
        nameField.setPlaceholder("Bezeichnung eingeben");
        nameField.addClassName("dialog-input");

        // Beschreibung
        TextField beschreibungField = new TextField("Beschreibung");
        beschreibungField.setPlaceholder("Beschreibung eingeben");
        beschreibungField.addClassName("dialog-input");

        Checkbox isPremiumCheckbox = new Checkbox("nur für Premium");

        // Buttons
        Button saveButton = new Button("Speichern", event -> {
            if (nameField.isEmpty() || beschreibungField.isEmpty()) {
                Notification.show("Alle Felder müssen ausgefüllt werden!", 3000, Notification.Position.MIDDLE);
            } else {
                // Gerät speichern
                Geraet neuesGeraet = new Geraet();
                neuesGeraet.setName(nameField.getValue());
                neuesGeraet.setBeschreibung(beschreibungField.getValue());
                neuesGeraet.setPremium(isPremiumCheckbox.getValue());

                geraetService.geraetHinzufuegen(neuesGeraet);

                updateGrid();
                dialog.close();
            }
        });
        saveButton.getStyle().set("background-color", "#FFA500");
        saveButton.getStyle().set("color", "white");

        Button cancelButton = new Button("Abbrechen", event -> dialog.close());
        cancelButton.getStyle().set("color", "#007BFF");

        // HorizontalLayout für die Buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true); // Abstand zwischen den Buttons
        buttonLayout.setWidthFull(); // Optional: Button-Layout auf volle Breite setzen
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Buttons rechtsbündig ausrichten (optional)

        VerticalLayout dialogLayout = new VerticalLayout(nameField, beschreibungField, isPremiumCheckbox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private HorizontalLayout createActions(Geraet geraet) {
    	 
    	// Bearbeiten-Button mit Icon
        Button editButton = new Button();
        editButton.getElement().setProperty("innerHTML", "<i class='fa fa-edit' style='color: #FFA500;'></i>");
        editButton.getStyle().set("background-color", "DC3545");
        editButton.getStyle().set("border", "none");
        editButton.addClickListener(event -> openEditGeraetDialog(geraet));
        // Tooltip über das title-Attribut
        editButton.getElement().setAttribute("title", "Bearbeiten");
        
        
        // Löschen-Button mit Icon
        Button deleteButton = new Button();
        deleteButton.getElement().setProperty("innerHTML", "<i class='fa fa-trash' style='color: #DC3545;'></i>");
        deleteButton.getStyle().set("background-color", "DC3545");
        deleteButton.getStyle().set("border", "none");
        deleteButton.addClickListener(event -> openDeleteConfirmationDialog(geraet));

     // Tooltip über das title-Attribut
        deleteButton.getElement().setAttribute("title", "Löschen");

        
        
     // Info-Button mit Icon
        Button infoButton = new Button();
        infoButton.getElement().setProperty("innerHTML", "<i class='fa fa-info-circle' style='color: #007BFF;'></i>");
        infoButton.getStyle().set("background-color", "DC3545");
        infoButton.getStyle().set("border", "none");
        infoButton.addClickListener(event -> openInfoDialog(geraet));
     // Tooltip über das title-Attribut
        infoButton.getElement().setAttribute("title", "Details anzeigen");
        

        
        HorizontalLayout actionsLayout = new HorizontalLayout(infoButton, editButton, deleteButton);
        actionsLayout.setSpacing(true);
        return actionsLayout;
    }

    private void openInfoDialog(Geraet geraet) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Informationen");

        // Name anzeigen
        Label nameLabel = new Label("Name: " + geraet.getName());

        // Beschreibung anzeigen
        Label beschreibungLabel = new Label("Beschreibung: " + geraet.getBeschreibung());

        // Premium-Status anzeigen
        Label premiumLabel = new Label("Premium: " + (geraet.isPremium() ? "Ja" : "Nein"));

        Button closeButton = new Button("Schließen", event -> dialog.close());
        closeButton.getStyle().set("background-color", "#6C757D");
        closeButton.getStyle().set("color", "white");

        VerticalLayout dialogLayout = new VerticalLayout(nameLabel, beschreibungLabel, premiumLabel, closeButton);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }

    
    private void openEditGeraetDialog(Geraet geraet) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Gerät / Bereich bearbeiten");
        

        // Name
        TextField nameField = new TextField("Name");
        nameField.setValue(geraet.getName());
        nameField.addClassName("dialog-input");

        // Beschreibung
        TextField beschreibungField = new TextField("Beschreibung");
        beschreibungField.setValue(geraet.getBeschreibung());
        beschreibungField.addClassName("dialog-input");
        
        Checkbox isPremiumCheckbox = new Checkbox("nur für Premium");
        isPremiumCheckbox.setValue(geraet.isPremium());

        Button saveButton = new Button("Änderungen speichern", event -> {
            geraet.setName(nameField.getValue());
            geraet.setBeschreibung(beschreibungField.getValue());
            geraet.setPremium(isPremiumCheckbox.getValue());

            geraetService.geraetBearbeiten(geraet.getId(), geraet);

            updateGrid();
            dialog.close();
        });
        

        saveButton.getStyle().set("background-color", "#FFA500");
        saveButton.getStyle().set("color", "white");
        
     // Abbrechen-Button
        Button cancelButton = new Button("Abbrechen", event -> dialog.close());
        cancelButton.getStyle().set("color", "#007BFF"); // Blau für Abbrechen

        
        // HorizontalLayout für die Buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true); // Abstand zwischen den Buttons
        buttonLayout.setWidthFull(); // Optional: Button-Layout auf volle Breite setzen
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Buttons rechtsbündig ausrichten (optional)

        
        VerticalLayout dialogLayout = new VerticalLayout(nameField, beschreibungField, isPremiumCheckbox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }
    
    // Löschen Button Dialog
    private void openDeleteConfirmationDialog(Geraet geraet) {
        // Dialog erstellen
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Gerät / Bereich löschen");

        // Nachricht
        VerticalLayout messageLayout = new VerticalLayout();
        messageLayout.add(new com.vaadin.flow.component.html.Label("Möchtest Du dieses Element wirklich löschen?"));

        // Bestätigungs-Button
        Button confirmButton = new Button("Löschen", event -> {
        	try {
            geraetService.geraetLoeschen(geraet.getId()); // Gerät löschen
            updateGrid(); // Grid aktualisieren 
            
        	} catch (RuntimeException ex) {
                Notification.show(ex.getMessage(), 3500, Notification.Position.MIDDLE);
            }
        	
        	dialog.close(); // Dialog schließen
        });
        confirmButton.getStyle().set("background-color", "#FF0000"); // Roter Hintergrund
        confirmButton.getStyle().set("color", "white");

        // Abbrechen-Button
        Button cancelButton = new Button("Abbrechen", event -> dialog.close());
        cancelButton.getStyle().set("color", "#007BFF"); // Blau für Abbrechen

        // Layout für Buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.setSpacing(true);

        // Dialog zusammensetzen
        VerticalLayout dialogLayout = new VerticalLayout(messageLayout, buttonLayout);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);

        dialog.open(); // Dialog öffnen
    }
}
