package com.example.demo.views.uebung;

//Author: Fabian Müller


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

import com.example.demo.model.entities.Uebung;
import com.example.demo.model.enums.Muskelgruppe;
import com.example.demo.model.entities.Geraet;
import com.example.demo.service.UebungService;
import com.example.demo.service.GeraetService;

import com.example.demo.views.ManagerLayout;


import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.List;

@PageTitle("Übungenverwaltung")
@Route(value = "manager/uebungen", layout = ManagerLayout.class)
@PermitAll
public class ManagerUebungenView extends VerticalLayout {

    private final UebungService uebungService;
    private final GeraetService geraetService;
    private Grid<Uebung> uebungGrid;

    @Autowired
    public ManagerUebungenView(UebungService uebungService, GeraetService geraetService) {
        this.uebungService = uebungService;
        this.geraetService = geraetService;

        setSpacing(true);
        setPadding(true);

        // Button: Neue Übung hinzufügen
        Button addUebungButton = new Button("Übung hinzufügen", event -> openAddUebungDialog());
        addUebungButton.getStyle().set("background-color", "#28a745");
        addUebungButton.getStyle().set("color", "white");

        HorizontalLayout headerLayout = new HorizontalLayout(addUebungButton);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.END);

        // Grid: Übungsliste
        uebungGrid = new Grid<>(Uebung.class, false);
        uebungGrid.addColumn(Uebung::getId).setHeader("ID").setWidth("100px").setFlexGrow(0);
        uebungGrid.addColumn(Uebung::getName).setHeader("Name");
        uebungGrid.addColumn(Uebung::getBeschreibung).setHeader("Beschreibung");
        uebungGrid.addColumn(uebung -> uebung.getMuskelgruppe() != null ? uebung.getMuskelgruppe().name() : "Keine Muskelgruppe")
        .setHeader("Muskelgruppe")
        .setFlexGrow(1);

        uebungGrid.addColumn(uebung -> uebung.getGeraet() != null ? uebung.getGeraet().getName() : "Kein Gerät")
                  .setHeader("Gerät / Bereich");
        uebungGrid.addComponentColumn(this::createActions).setHeader("Aktionen");

        // Übungen aus der Datenbank laden
        updateGrid();

        add(headerLayout, uebungGrid);
    }

    private void updateGrid() {
        List<Uebung> uebungen = uebungService.alleUebungenAbrufen();
        uebungGrid.setItems(uebungen);
    }

    private void openAddUebungDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Neue Übung hinzufügen");
        dialog.setWidth("400px");
        dialog.setHeight("auto");

        TextField nameField = new TextField("Name");
        nameField.setPlaceholder("Übungsname eingeben");
        nameField.addClassName("dialog-input");

        TextField beschreibungField = new TextField("Beschreibung");
        beschreibungField.setPlaceholder("Übungsbeschreibung eingeben");
        beschreibungField.addClassName("dialog-input");
        
        ComboBox<Muskelgruppe> muskelgruppeComboBox = new ComboBox<>("Muskelgruppe");
        muskelgruppeComboBox.setItems(Muskelgruppe.values());
        muskelgruppeComboBox.setPlaceholder("Muskelgruppe auswählen");
        muskelgruppeComboBox.addClassName("dialog-input");


        ComboBox<Geraet> geraetComboBox = new ComboBox<>("Gerät / Bereich (optional)");
        geraetComboBox.setItems(geraetService.alleGeraeteAbrufen());
        geraetComboBox.setItemLabelGenerator(Geraet::getName);
        geraetComboBox.setPlaceholder("Gerät / Bereich aus Liste auswählen");
        geraetComboBox.addClassName("dialog-input");

        // Buttons
        Button saveButton = new Button("Übung speichern", event -> {
            if (nameField.isEmpty() || beschreibungField.isEmpty()) {
                Notification.show("Alle Felder müssen ausgefüllt werden!", 3000, Notification.Position.MIDDLE);
            } else {
                Uebung neueUebung = new Uebung();
                neueUebung.setName(nameField.getValue());
                neueUebung.setBeschreibung(beschreibungField.getValue());
                neueUebung.setMuskelgruppe(muskelgruppeComboBox.getValue()); 
                neueUebung.setGeraet(geraetComboBox.getValue());

                uebungService.uebungHinzufuegen(neueUebung);

                updateGrid();
                dialog.close();
            }
        });
        saveButton.getStyle().set("background-color", "#FFA500");
        saveButton.getStyle().set("color", "white");

        Button cancelButton = new Button("Abbrechen", event -> dialog.close());
        cancelButton.getStyle().set("color", "#007BFF");

        // Button Layout
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(nameField, beschreibungField, muskelgruppeComboBox, geraetComboBox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private HorizontalLayout createActions(Uebung uebung) {
        Button editButton = new Button();
        editButton.getElement().setProperty("innerHTML", "<i class='fa fa-edit' style='color: #FFA500;'></i>");
        editButton.getStyle().set("border", "none");
        editButton.addClickListener(event -> openEditUebungDialog(uebung));
        editButton.getElement().setAttribute("title", "Bearbeiten");
        

        Button deleteButton = new Button();
        deleteButton.getElement().setProperty("innerHTML", "<i class='fa fa-trash' style='color: #DC3545;'></i>");
        deleteButton.getStyle().set("border", "none");
        deleteButton.addClickListener(event -> openDeleteConfirmationDialog(uebung));
        deleteButton.getElement().setAttribute("title", "Löschen");

        Button infoButton = new Button();
        infoButton.getElement().setProperty("innerHTML", "<i class='fa fa-info-circle' style='color: #007BFF;'></i>");
        infoButton.getStyle().set("border", "none");
        infoButton.addClickListener(event -> openInfoDialog(uebung));
        infoButton.getElement().setAttribute("title", "Details anzeigen");

        HorizontalLayout actionsLayout = new HorizontalLayout(infoButton, editButton, deleteButton);
        actionsLayout.setSpacing(true);
        return actionsLayout;
    }

    private void openEditUebungDialog(Uebung uebung) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Übung bearbeiten");
        dialog.setWidth("400px");
        dialog.setHeight("auto");

        TextField nameField = new TextField("Name");
        nameField.setValue(uebung.getName());
        nameField.addClassName("dialog-input");

        TextField beschreibungField = new TextField("Beschreibung");
        beschreibungField.setValue(uebung.getBeschreibung());
        beschreibungField.addClassName("dialog-input");
        
        ComboBox<Muskelgruppe> muskelgruppeComboBox = new ComboBox<>("Muskelgruppe");
        muskelgruppeComboBox.setItems(Muskelgruppe.values());
        muskelgruppeComboBox.setValue(uebung.getMuskelgruppe()); 
        muskelgruppeComboBox.addClassName("dialog-input");

        ComboBox<Geraet> geraetComboBox = new ComboBox<>("Gerät / Bereich (optional)");
        geraetComboBox.setItems(geraetService.alleGeraeteAbrufen());
        geraetComboBox.setItemLabelGenerator(Geraet::getName);
        geraetComboBox.setValue(uebung.getGeraet());
        geraetComboBox.addClassName("dialog-input");

        Button saveButton = new Button("Änderungen speichern", event -> {
            uebung.setName(nameField.getValue());
            uebung.setBeschreibung(beschreibungField.getValue());
            uebung.setMuskelgruppe(muskelgruppeComboBox.getValue());
            uebung.setGeraet(geraetComboBox.getValue());

            uebungService.uebungBearbeiten(uebung.getId(), uebung);

            updateGrid();
            dialog.close();
        });
        saveButton.getStyle().set("background-color", "#FFA500");
        saveButton.getStyle().set("color", "white");

        Button cancelButton = new Button("Abbrechen", event -> dialog.close());
        cancelButton.getStyle().set("color", "#007BFF");

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(nameField, beschreibungField, muskelgruppeComboBox, geraetComboBox, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openDeleteConfirmationDialog(Uebung uebung) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Übung löschen");

        Label confirmationText = new Label("Möchten Sie diese Übung wirklich löschen?");

        Button confirmButton = new Button("Löschen", event -> {
            try {
                uebungService.uebungLoeschen(uebung.getId());
                updateGrid();
            } catch (RuntimeException ex) {
                Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
            dialog.close();
        });
        confirmButton.getStyle().set("background-color", "#DC3545");
        confirmButton.getStyle().set("color", "white");

        Button cancelButton = new Button("Abbrechen", event -> dialog.close());
        cancelButton.getStyle().set("color", "#007BFF");

        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.setSpacing(true);

        VerticalLayout dialogLayout = new VerticalLayout(confirmationText, buttonLayout);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openInfoDialog(Uebung uebung) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Übungsdetails");

        Label nameLabel = new Label("Name: " + uebung.getName());
        Label beschreibungLabel = new Label("Beschreibung: " + uebung.getBeschreibung());
        Label muskelgruppeLabel = new Label("Muskelgruppe: " + (uebung.getMuskelgruppe() != null ? uebung.getMuskelgruppe().name() : "Keine Muskelgruppe"));
        Label geraetLabel = new Label("Gerät: " + (uebung.getGeraet() != null ? uebung.getGeraet().getName() : "Kein Gerät"));

        Button closeButton = new Button("Schließen", event -> dialog.close());
        closeButton.getStyle().set("background-color", "#6C757D");
        closeButton.getStyle().set("color", "white");

        VerticalLayout dialogLayout = new VerticalLayout(nameLabel, beschreibungLabel, muskelgruppeLabel, geraetLabel, closeButton);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }
}
