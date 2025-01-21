package com.example.demo.views.Dashboards;

import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
            UI.getCurrent().navigate("trainingshistorie");
        });
        Button btnTrainingStarten = new Button(bundle.getString("mitglied.dashboard.trainingStarten"), event -> {
            UI.getCurrent().navigate("trainingsplanauswahl");
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
        H1 title = new H1(bundle.getString("mitglied.dashboard.title"));
        title.getStyle().set("user-select", "none");
        title.getStyle().set("pointer-events", "none");

        VerticalLayout mainLayout = new VerticalLayout(title, buttonLayout);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setSpacing(true);

        add(mainLayout);
    }
}