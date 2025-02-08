package com.example.demo.views.training;

//Author: Ömer Yalcinkaya

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.entities.Training;
import com.example.demo.model.entities.Trainingshistorie;
import com.example.demo.model.entities.Uebung;
import com.example.demo.service.TrainingService;
import com.example.demo.service.TrainingshistorieService;
import com.example.demo.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.MultiSortPriority;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@PageTitle("Trainingshistorie")
@Route(value = "trainingshistorie"/*, layout = MainLayout.class */)
@PermitAll
@Uses(Icon.class)
public class TrainingshistorieView extends VerticalLayout{

    private TrainingshistorieService trainingshistorieService;
    private TrainingService trainingService;
    private UserService userService;
    private H2 title = new H2("Trainingshistorie");
    private Grid<Trainingshistorie> grid = new Grid<>(Trainingshistorie.class, false);
    private Button btnOpen = new Button("Trainingshistorie öffnen");
    private HorizontalLayout hlButtons = new HorizontalLayout(btnOpen);
    private Dialog historieDialog = new Dialog();
    private TextField tfName = new TextField("Name");
    private TextField tfDauer = new TextField("Dauer");
    private TextField tfDatum = new TextField("Datum");
    private TextField tfSumUebungen = new TextField("Anzahl Übungen");
    private TextField tfSumSaetze = new TextField("Anzahl Sätze");
    private TextField tfSumWdh = new TextField("Anzahl Wiederholungen");
    private TextField tfSumGewicht = new TextField("Gesamtgewicht in kg");
    private TextField tfAvgGewicht = new TextField("Durchschnittsgewicht in kg");
    private VerticalLayout vlDialogLayout = new VerticalLayout(tfName, tfDauer, tfDatum, tfSumUebungen, tfSumSaetze, tfSumWdh, tfSumGewicht, tfAvgGewicht);


    @Autowired
    public TrainingshistorieView(TrainingshistorieService trainingshistorieService, TrainingService trainingService, UserService userService) {
        this.trainingshistorieService = trainingshistorieService;
        this.trainingService = trainingService;
        this.userService = userService;

        // Alle Textfelder schreibgeschützt
        tfName.setReadOnly(true);
        tfDauer.setReadOnly(true);
        tfDatum.setReadOnly(true);
        tfSumUebungen.setReadOnly(true);
        tfSumSaetze.setReadOnly(true);
        tfSumWdh.setReadOnly(true);
        tfSumGewicht.setReadOnly(true);
        tfAvgGewicht.setReadOnly(true);

        // Grid-Konfiguration
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(training -> training.getTraining().getName())
            .setHeader("Training")
            .setSortable(true);
        grid.addColumn(training -> training.getDatum().format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yy")))
            .setHeader("Datum")
            .setSortable(true);
        grid.addColumn(Trainingshistorie::getDauer_string).setHeader("Dauer").setSortable(true);
        grid.setItems(trainingshistorieService.findByUserId(userService.getCurrentUser().getId()));
        grid.setMultiSort(true, MultiSortPriority.APPEND);
        grid.setSizeFull();
        grid.setHeight("auto");
        grid.setMinHeight("500px");

        // Öffnen-Button zum Anzeigen der Details
        btnOpen.addClickListener(e -> {
            if (grid.asSingleSelect().isEmpty()) {
                Notification.show("Bitte wählen Sie eine Trainingshistorie aus", 3000, Notification.Position.MIDDLE);
            } else {
                Trainingshistorie selected = grid.asSingleSelect().getValue();
                tfName.setValue(selected.getTraining().getName() != null ? selected.getTraining().getName() : "--");
                tfDauer.setValue(selected.getDauer_string() != null ? selected.getDauer_string() : "--");
                tfDatum.setValue(selected.getDatum() != null ? selected.getDatum().format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yy")) : "--");
                tfSumUebungen.setValue(selected.getSumUebungen() != 0 ? String.valueOf(selected.getSumUebungen()) : "--");
                tfSumSaetze.setValue(selected.getSumSaetze() != 0 ? String.valueOf(selected.getSumSaetze()) : "--");
                tfSumWdh.setValue(selected.getSumWdh() != 0 ? String.valueOf(selected.getSumWdh()) : "--");
                tfSumGewicht.setValue(selected.getSumGewicht() != 0 ? String.valueOf(selected.getSumGewicht()) : "--");
                tfAvgGewicht.setValue(selected.getAvgGewicht() != 0 ? String.valueOf(selected.getAvgGewicht()) : "--");
                historieDialog.open();
            }
        });

        // Dialog-Konfiguration
        historieDialog.setCloseOnEsc(true);
        historieDialog.setCloseOnOutsideClick(true);
        historieDialog.setResizable(true);
        historieDialog.setDraggable(false);
        historieDialog.add(vlDialogLayout);
        historieDialog.getFooter().add(new Button("Schließen", e -> historieDialog.close()));
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("60%");
        getStyle().set("margin", "auto");
        add(title, grid, hlButtons, historieDialog);
    }
}
