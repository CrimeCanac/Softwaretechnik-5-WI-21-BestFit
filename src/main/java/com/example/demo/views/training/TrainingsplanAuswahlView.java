package com.example.demo.views.training;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.entities.Training;
import com.example.demo.model.entities.Trainingsplan;
import com.example.demo.service.TrainingService;
import com.example.demo.service.TrainingsplanService;
import com.vaadin.copilot.javarewriter.JavaRewriter.AlignmentMode;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@PageTitle("Trainingsplan auswählen")
@Route(value = "trainingsplanauswahl"/*, layout = MainLayout.class */)
@PermitAll
@Uses(Icon.class)
public class TrainingsplanAuswahlView extends VerticalLayout{

	private TrainingsplanService trainingsplanService;
    private TrainingService trainingService;
    private VerticalLayout vlGrid = new VerticalLayout();
    private Grid<Trainingsplan> gridTrainingsplan = new Grid<>(Trainingsplan.class, false);
    private HorizontalLayout hlToolbar = new HorizontalLayout();
    private TextField tfFilterName = new TextField();
    private Button btnChoose = new Button("Trainingsplan auswählen");
    private Button btnExportPdf = new Button("PDF Exportieren", VaadinIcon.FILE_TEXT.create());
    private HorizontalLayout hlButtons_Trainingsplan = new HorizontalLayout(btnChoose, btnExportPdf);
    private Dialog trainingDialog = new Dialog();
    private Grid<Training> gridTraining = new Grid<>(Training.class, false);
    private Button btnStarten = new Button("Training starten");
    private Button btnAbbrechen = new Button("Abbrechen");
    private HorizontalLayout hlButtons_Training = new HorizontalLayout(btnStarten, btnAbbrechen);

    public TrainingsplanAuswahlView(@Autowired TrainingsplanService trainingsplanService, @Autowired TrainingService trainingService) {
        this.trainingsplanService = trainingsplanService;
        this.trainingService = trainingService;

        // PDF Export-Button konfigurieren
        btnExportPdf.addClickListener(e -> {
            Trainingsplan selectedTrainingsplan = getSelectedTrainingsplan();
            if (selectedTrainingsplan != null) {
                String exportUrl = "/trainingsplan/" + selectedTrainingsplan.getId() + "/uebungen";
                getUI().ifPresent(ui -> ui.getPage().open(exportUrl));
            } else {
                Notification.show("Bitte wählen Sie einen Trainingsplan aus, um ihn zu exportieren.", 3000, Notification.Position.MIDDLE);
            }
        });
        btnExportPdf.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnChoose.addClickListener(e -> {
            Trainingsplan selectedTrainingsplan = getSelectedTrainingsplan();
            if (selectedTrainingsplan != null) {
                gridTraining.setItems(trainingService.getTrainingsByTrainingsplanId(selectedTrainingsplan.getId()));
                gridTraining.setColumns("name", "beschreibung");
                gridTraining.setHeight("95%");
                trainingDialog.open();
            } else {
                Notification.show("Bitte wählen Sie einen Trainingsplan aus.", 3000, Notification.Position.MIDDLE);
            }
        });
        btnStarten.addClickListener(e -> {
            Training selectedTraining = gridTraining.asSingleSelect().getValue();
            if (selectedTraining != null) {
                getUI().ifPresent(ui -> ui.navigate("trainingview/" + selectedTraining.getId()));
            } else {
                Notification.show("Bitte wählen Sie ein Training aus.", 3000, Notification.Position.MIDDLE);
            }
        });
        btnAbbrechen.addClickListener(e -> {
            trainingDialog.close();
        });
        tfFilterName.setLabel("Filtern nach Name");
        tfFilterName.setPlaceholder("Name");
        tfFilterName.setClearButtonVisible(true);
        tfFilterName.setValueChangeMode(ValueChangeMode.LAZY);
        tfFilterName.addValueChangeListener(e -> {
            gridTrainingsplan.setItems(
                trainingsplanService.filterTrainingsplanByName(tfFilterName.getValue()));
        });
        hlToolbar.add(tfFilterName);
        hlToolbar.setAlignItems(Alignment.BASELINE);
        gridTrainingsplan.setItems(trainingsplanService.getAllTrainingsplaene());
        gridTrainingsplan.setColumns("name", "beschreibung");
        trainingDialog.setWidth("40%");
        trainingDialog.setHeight("40%");
        trainingDialog.add(gridTraining);
        trainingDialog.getFooter().add(hlButtons_Training);
        trainingDialog.setCloseOnEsc(false);
        trainingDialog.setCloseOnOutsideClick(false);
        trainingDialog.setModal(true);
        trainingDialog.setHeaderTitle("Training auswählen");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        vlGrid.setWidth("50%");
        hlButtons_Training.setSizeFull();
        hlButtons_Training.setJustifyContentMode(JustifyContentMode.START);
        hlButtons_Training.setAlignItems(Alignment.BASELINE);
        btnStarten.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAbbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);   

        vlGrid.add(hlToolbar, gridTrainingsplan);
        add(vlGrid, hlButtons_Trainingsplan, trainingDialog);
    }

    public Trainingsplan getSelectedTrainingsplan() {
        return gridTrainingsplan.asSingleSelect().getValue();
    }
}