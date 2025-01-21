package com.example.demo.views.Dashboards;
// Author: Delbrin Alazo

// Created: 2025-01-15
// Modified by: Delbrin Alazo
// Description: Admin Dashboard with buttons for managing exercises, training plans, profiles, employees, members, and logout

import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@PageTitle("Admin Dashboard")
@Route(value = "admin-dashboard")
@RolesAllowed("ADMINISTRATOR")
public class AdminDashboard extends VerticalLayout {

    public AdminDashboard() {
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
        Button btnProfilBearbeiten = new Button(bundle.getString("mitglied.dashboard.profilBearbeiten"), event -> {
        });
        Button btnLogout = new Button(bundle.getString("mitglied.dashboard.logout"), event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        btnLogout.getStyle().set("background-color", "red");
        btnLogout.getStyle().set("color", "white");

        // Additional buttons for admin
        Button btnMitarbeiterVerwalten = new Button("Mitarbeiter Verwalten", event -> {
        });
        Button btnMitgliederVerwalten = new Button("Mitglieder Verwalten", event -> {
        });

        // Create a horizontal layout for the buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(btnUebungen, btnTrainingsplaene,
                btnProfilBearbeiten, btnMitarbeiterVerwalten, btnMitgliederVerwalten, btnLogout);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setSpacing(true);

        // Add title and button layout to the main layout
        H1 title = new H1("Admin Dashboard");
        title.getStyle().set("user-select", "none");
        title.getStyle().set("pointer-events", "none");

        VerticalLayout mainLayout = new VerticalLayout(title, buttonLayout);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setSpacing(true);

        add(mainLayout);
    }
}