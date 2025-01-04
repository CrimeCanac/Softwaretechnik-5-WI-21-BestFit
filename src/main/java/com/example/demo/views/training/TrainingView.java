


package com.example.demo.views.training;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.entities.Training;
import com.example.demo.model.entities.Trainingshistorie;
import com.example.demo.model.entities.Uebung;
import com.example.demo.service.TrainingService;
import com.example.demo.service.TrainingshistorieService;
import com.example.demo.service.UserService;
import com.example.demo.views.uebung.UebungLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Minus.Vertical;

import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Training")
@Route(value = "trainingview/:trainingID")
@PermitAll
@Uses(Icon.class)
public class TrainingView extends VerticalLayout implements BeforeEnterObserver {

    private H2 title = new H2("Training");
    private StopuhrView stopuhr = new StopuhrView();
    private List<Uebung> uebungen = new ArrayList<>();
    private List<VerticalLayout> uebungComponents = new ArrayList<>();
    private Training training;
    private TrainingService trainingService;
    private TrainingshistorieService trainingshistorieService;
    private UserService userService;
    private String trainingIDString;
    private VerticalLayout vlUebungen = new VerticalLayout();
    private Button btnBeenden = new Button("Beenden", VaadinIcon.CHECK.create());
    private Button btnAbbrechen = new Button("Abbrechen", VaadinIcon.CLOSE.create());
    private Button btnExportPdf = new Button("PDF Exportieren", VaadinIcon.FILE_TEXT.create());

    @Autowired
    public TrainingView(TrainingService trainingService, 
                        TrainingshistorieService trainingshistorieService,
                        UserService userService) {
        this.trainingService = trainingService;
        this.trainingshistorieService = trainingshistorieService;
        this.userService = userService;

        // PDF-Export-Button konfigurieren
        btnExportPdf.addClickListener(e -> {
            String exportUrl = "/trainingsplan/" + trainingIDString + "/uebungen";
            UI.getCurrent().getPage().open(exportUrl);
        });
        btnExportPdf.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Event-Handler für andere Buttons
        btnBeenden.addClickListener(e -> {
            Trainingshistorie th = new Trainingshistorie();
            stopuhr.stop();
            stopuhr.syncTimeAndUpdateBackend(th, trainingshistorieService);
            th.setTraining(training);
            th.setSumGewicht(getSumGewicht());
            th.setSumSaetze(getSumSaetze());
            th.setSumWdh(getSumWdh());
            th.setSumUebungen(getSumUebungen());
            th.setUser(userService.getCurrentUser());
            trainingshistorieService.saveTrainingshistorie(th);
            UI.getCurrent().navigate("home");
        });
        btnAbbrechen.addClickListener(e -> {
            stopuhr.stop();
            UI.getCurrent().navigate("trainingsplanauswahl");
        });
        btnBeenden.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        btnAbbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        // Layout-Konfiguration
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("100%");
        vlUebungen.setMargin(false);
        vlUebungen.setPadding(false);
        vlUebungen.setSpacing(false);

        // Elemente zur Ansicht hinzufügen
        add(title, stopuhr, new Hr(), vlUebungen, new Hr(), 
            new HorizontalLayout(btnExportPdf, btnBeenden, btnAbbrechen));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        trainingIDString = event.getRouteParameters().get("trainingID").orElse("");
        setTraining(trainingService.getTrainingById(Long.parseLong(trainingIDString)));
        setUebungen();
        setLayouts();
        for (VerticalLayout x : uebungComponents) {
            vlUebungen.add(x);
        }
    }

    public void setUebungen() {
        uebungen = training.getUebungen();
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    private void setLayouts() {
        if (uebungen == null || uebungen.isEmpty()) {
            H2 h2 = new H2("Keine Übungen vorhanden");
            VerticalLayout vl = new VerticalLayout(h2);
            vl.setAlignItems(Alignment.CENTER);
            vl.setJustifyContentMode(JustifyContentMode.CENTER);
            uebungComponents.add(vl);
            btnBeenden.setEnabled(false);        
        }
        for (Uebung uebung : uebungen) {
            uebungComponents.add(new UebungLayout(uebung, 3));
        }
    }

    public int getSumUebungen() {
        return uebungen.size();
    }

    public int getSumSaetze() {
        int sum = 0;
        for (VerticalLayout x : uebungComponents) {
            UebungLayout y = (UebungLayout) x;
            sum += y.getSumSaetze();
        }
        return sum;
    }

    public int getSumWdh() {
        int sum = 0;
        for (VerticalLayout x : uebungComponents) {
            UebungLayout y = (UebungLayout) x;
            sum += y.getSumWdh();
        }
        return sum;
    }

    public double getSumGewicht() {
        int sum = 0;
        for (VerticalLayout x : uebungComponents) {
            UebungLayout y = (UebungLayout) x;
            sum += y.getSumGewicht();
        }
        return sum;
    }
}
