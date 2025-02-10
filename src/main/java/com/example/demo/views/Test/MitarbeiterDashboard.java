package com.example.demo.views.Test;

// Author: Delbrin Alazo

// Created: 2025-01-14
// Last Updated: 2025-01-31
// Modified by: Delbrin Alazo
// Description: Mitarbeiter dashboard view

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

@PageTitle("Mitarbeiter Dashboard")
@Route(value = "mitarbeiter-dashboard")
@RolesAllowed("MITARBEITER")
public class MitarbeiterDashboard extends VerticalLayout {

    public MitarbeiterDashboard() {
        // Load I18n texts
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("messages", UI.getCurrent().getLocale());
        } catch (MissingResourceException e) {
            // Fallback to English if no bundle / error occurs
            bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        }

        // Create buttons with I18n texts
        Button btnUebungenVerwalten = new Button(bundle.getString("mitarbeiter.dashboard.uebungenVerwalten"), event -> {
            UI.getCurrent().navigate("admin/uebungen");
        });

        Button btnGeraeteVerwalten = new Button(bundle.getString("mitarbeiter.dashboard.geraeteVerwalten"), event -> {
            UI.getCurrent().navigate("admin/geraete");
        });

        Button btnTrainingsplaeneVerwalten = new Button(
                bundle.getString("mitarbeiter.dashboard.trainingsplaeneVerwalten"), event -> {
                });

        Button btnMitgliederVerwalten = new Button(bundle.getString("mitarbeiter.dashboard.mitgliederVerwalten"),
                event -> {
                    UI.getCurrent().navigate("mitglieder-verwalten");
                });
        Button btnLogout = new Button(bundle.getString("mitarbeiter.dashboard.logout"), event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        btnLogout.getStyle().set("background-color", "red");
        btnLogout.getStyle().set("color", "white");

        // Create a horizontal layout for the buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(btnUebungenVerwalten, btnGeraeteVerwalten,
                btnTrainingsplaeneVerwalten,
                btnMitgliederVerwalten, btnLogout);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setSpacing(true);

        // Add title and button layout to the main layout
        H1 title = new H1(bundle.getString("mitarbeiter.dashboard.title"));
        title.getStyle().set("user-select", "none");
        title.getStyle().set("pointer-events", "none");

        VerticalLayout mainLayout = new VerticalLayout(title, buttonLayout);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setSpacing(true);

        add(mainLayout);
    }
}