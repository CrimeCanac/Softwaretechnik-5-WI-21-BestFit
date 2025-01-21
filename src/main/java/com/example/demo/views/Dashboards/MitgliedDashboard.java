package com.example.demo.views.Dashboards;
// Author: Delbrin Alazo

// Created: 2025-01-15
// Modified by: Delbrin Alazo
// Description: Mitglied Dashboard mit Kalorien- und Proteinrechner

import jakarta.annotation.security.RolesAllowed;
import com.example.demo.service.Kalorienrechner;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@PageTitle("Mitglied Dashboard")
@Route(value = "mitglied-dashboard")
@RolesAllowed("MITGLIED")
public class MitgliedDashboard extends VerticalLayout {

    public MitgliedDashboard() {
        // Load I18n texts
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("messages", UI.getCurrent().getLocale());
        } catch (MissingResourceException e) {
            // Fallback to English if no bundle / error occurs
            bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        }

        // Create buttons with I18n texts
        Button btnUebungen = new Button(bundle.getString("mitglied.dashboard.uebungen"), event -> {
        });
        Button btnTrainingsplaene = new Button(bundle.getString("mitglied.dashboard.trainingsplaene"), event -> {
        });
        Button btnTrainingshistorie = new Button(bundle.getString("mitglied.dashboard.trainingshistorie"), event -> {
        });
        Button btnTrainingStarten = new Button(bundle.getString("mitglied.dashboard.trainingStarten"), event -> {
        });
        Button btnProfilBearbeiten = new Button(bundle.getString("mitglied.dashboard.profilBearbeiten"), event -> {
        });
        Button btnLogout = new Button(bundle.getString("mitglied.dashboard.logout"), event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        btnLogout.getStyle().set("background-color", "red");
        btnLogout.getStyle().set("color", "white");

        // Create a horizontal layout for the buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(btnUebungen, btnTrainingsplaene, btnTrainingshistorie,
                btnTrainingStarten, btnProfilBearbeiten, btnLogout);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setSpacing(true);

        // Add title and button layout to the main layout
        VerticalLayout mainLayout = new VerticalLayout(new H1(bundle.getString("mitglied.dashboard.title")), buttonLayout);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setSpacing(true);

        add(mainLayout);

        // Create fields for the calculator
        NumberField weightField = new NumberField("Gewicht (kg)");
        NumberField heightField = new NumberField("Größe (cm)");
        NumberField ageField = new NumberField("Alter");
        ComboBox<String> activityLevelField = new ComboBox<>("Aktivitätslevel", "1", "2", "3", "4", "5");
        Button calculateButton = new Button("Berechnen");
        Label resultLabel = new Label();

        // Add click listener to the calculate button
        calculateButton.addClickListener(event -> {
            double weight = weightField.getValue();
            double height = heightField.getValue();
            double age = ageField.getValue();
            int activityLevel = activityLevelField.getValue().charAt(0) - '0'; // Extract the numeric value

            double grundumsatz = Kalorienrechner.berechneGrundumsatz(weight, height, age);
            double calorieNeeds = Kalorienrechner.berechneKalorienbedarf(grundumsatz, activityLevel);
            double proteinNeeds = Kalorienrechner.berechneProteinbedarf(weight);

            resultLabel.setText("Kalorienbedarf: " + calorieNeeds + " kcal, Proteinbedarf: " + proteinNeeds + " g");
        });

        // Add the calculator fields and button to the layout
        VerticalLayout calculatorLayout = new VerticalLayout(weightField, heightField, ageField, activityLevelField,
                calculateButton, resultLabel);
        calculatorLayout.setAlignItems(Alignment.CENTER);
        calculatorLayout.setSpacing(true);

        // Create a horizontal layout to place the calculator on the right
        HorizontalLayout mainContentLayout = new HorizontalLayout();
        mainContentLayout.setWidthFull();

        // Add a spacer to push the calculator to the right
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setWidth("50%"); // Can adjust the width as needed

        mainContentLayout.add(spacer, calculatorLayout);
        mainContentLayout.setAlignItems(Alignment.CENTER);

        // Add the main content layout to the main view
        add(mainContentLayout);
    }
}