package com.example.demo.views.Dashboards;

import com.example.demo.service.Kalorienrechner;
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
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Mitglied Dashboard")
@Route(value = "mitglied-dashboard")
@RolesAllowed("MITGLIED")
public class MitgliedDashboard extends VerticalLayout {

    public MitgliedDashboard() {
        // Create buttons
        Button btnTrainingsplaene = new Button("Meine Trainingspläne");
        Button btnTrainingshistorie = new Button("Meine Trainingshistorie");
        Button btnTrainingStarten = new Button("Training starten");

        // Create logout button
        Button btnLogout = new Button("Logout", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        btnLogout.getStyle().set("background-color", "red");
        btnLogout.getStyle().set("color", "white");

        // Create a horizontal layout for the buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(btnTrainingsplaene, btnTrainingshistorie,
                btnTrainingStarten, btnLogout);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setSpacing(true);

        // Create a horizontal layout for the title and center it
        HorizontalLayout titleLayout = new HorizontalLayout(new H1("Mitglied Dashboard"));
        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.setWidthFull();

        // Create a main layout to hold the title and button layouts
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.add(titleLayout, buttonLayout);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setSpacing(false);

        // Add the main layout to the main view
        add(mainLayout);
        setSizeFull();

        // Create fields for the calorie and protein calculator
        NumberField weightField = new NumberField("Gewicht (kg)");
        NumberField heightField = new NumberField("Größe (cm)");
        NumberField ageField = new NumberField("Alter");

        // Create activity level field with descriptions
        ComboBox<String> activityLevelField = new ComboBox<>("Aktivitätslevel");
        activityLevelField.setItems(
                "1: Sehr wenig aktiv (Kein Sport, Bürojob)",
                "2: Wenig aktiv (Etwas spazieren, Bürojob)",
                "3: Mäßig aktiv (Sport, Bürojob)",
                "4: Sehr aktiv (Sport, körperliche Arbeit)",
                "5: Extrem aktiv (Intensiver Sport, körperliche Arbeit)");

        Button calculateButton = new Button("Berechnen");
        Label resultLabel = new Label();

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