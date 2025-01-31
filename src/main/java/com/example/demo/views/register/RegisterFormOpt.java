package com.example.demo.views.register;

// Author: Delbrin Alazo
// Created: 2024-12-07
// Last Updated: 2025-01-31
// Modified by: Delbrin Alazo
// Description: register form optional for additional user information

import com.example.demo.model.entities.User;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.value.ValueChangeMode;

public class RegisterFormOpt extends VerticalLayout {

    FormLayout layoutRegisterPage = new FormLayout();
    HorizontalLayout layoutButtons = new HorizontalLayout();

    H1 header = new H1("Ergänzende Profilinformationen");
    Paragraph pBeschreibung = new Paragraph("Vervollständigen Sie ihr Profil.");
    NumberField tfGroesse = new NumberField("Größe (in cm)");
    NumberField tfGewicht = new NumberField("Gewicht (in kg)");

    Button btSkip = new Button("Überspringen");
    Button btSpeichern = new Button("Speichern");

    ProgressBar pRegistration = new ProgressBar();

    public RegisterFormOpt(RegisterView registerView, UserService userService, AuthenticatedUser authenticatedUser) {

        layoutRegisterPage.addClassName("register-form-opt");

        stylingComponentsCss();
        functionForFields();

        btSkip.addClickListener(e -> {
            UI.getCurrent().navigate("login");
            Notification.show("Profilinformationen wurden übersprungen. Sie können diese später im Profil ergänzen.");
            Notification.show("Sie können sich jetzt einloggen.");
        });

        btSpeichern.addClickListener(e -> {
            buttonSpeichernFunktion(registerView, userService, authenticatedUser);
        });
    }

    // Styling of components
    private void stylingComponentsCss() {

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        layoutRegisterPage.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1));

        layoutRegisterPage.getStyle().set("box-shadow", "0 0 10px grey");
        layoutRegisterPage.getStyle().set("border-radius", "6px");
        layoutRegisterPage.getStyle().set("padding", "3vh");
        layoutRegisterPage.getStyle().set("margin", "auto");
        layoutRegisterPage.getStyle().set("padding-bottom", "20px");

        layoutButtons.add(btSkip, btSpeichern);
        layoutButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutButtons.getStyle().set("padding-top", "5%");

        header.setWidth("47vh");
        header.getStyle().set("padding-top", "3%");
        header.getStyle().set("text-align", "center");

        pBeschreibung.setWidth("50%");
        pBeschreibung.getStyle().set("text-align", "center");

        tfGroesse.setMin(100);
        tfGroesse.setMax(250);

        tfGewicht.setMin(40);
        tfGewicht.setMax(500);

        pRegistration.setValue(1);
        pRegistration.setWidth("50%");

        btSpeichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layoutRegisterPage.add(pRegistration, header, pBeschreibung, tfGroesse, tfGewicht, layoutButtons);

        add(layoutRegisterPage);
    }

    // Function for the fields
    private void functionForFields() {
        tfGroesse.setValueChangeMode(ValueChangeMode.LAZY);
        tfGewicht.setValueChangeMode(ValueChangeMode.LAZY);

        tfGroesse.addValueChangeListener(e -> {
            try {
                checkGroesse();
            } catch (Exception ex) {
                tfGroesse.setErrorMessage("Bitte geben Sie eine Größe zwischen 100 und 250 cm ein.");
            }
        });
        tfGewicht.addValueChangeListener(e -> {
            try {
                checkGewicht();
            } catch (Exception ex) {
                tfGewicht.setErrorMessage("Bitte geben Sie ein Gewicht zwischen 40 und 500 kg ein.");
            }
        });
    }

    private boolean checkGroesse() {
        if (tfGroesse.getValue() < 100 || tfGroesse.getValue() > 250 || tfGroesse.isEmpty()) {
            tfGroesse.setErrorMessage("Bitte geben Sie eine Größe zwischen 100 und 250 cm ein.");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkGewicht() {
        if (tfGewicht.getValue() < 40 || tfGewicht.getValue() > 500 || tfGewicht.isEmpty()) {
            tfGewicht.setErrorMessage("Bitte geben Sie ein Gewicht zwischen 40 und 500 kg ein.");
            return false;
        } else {
            return true;
        }
    }

    // Function for the save button
    private void buttonSpeichernFunktion(RegisterView registerView, UserService userService,
            AuthenticatedUser authenticatedUser) {
        try {
            if (tfGroesse.isEmpty() || tfGewicht.isEmpty()) {
                Notification.show("Bitte füllen Sie alle Felder aus oder überspringen Sie.");
                return;
            } else if (!checkGroesse() || !checkGewicht()) {
                Notification.show("Bitte geben Sie korrekte Werte ein.");
                return;
            }

            Notification.show("Profilinformationen wurden gespeichert.");

            // get the user from the form
            User user = registerView.getUserFormRegisterForm();

            // set the additional information
            user.setGroesseCm(tfGroesse.getValue());
            user.setGewichtKg(tfGewicht.getValue());

            userService.update(user);

            // Get the currently authenticated user
            User currentUser = authenticatedUser.get().orElse(null);
            if (currentUser != null) {
                String role = currentUser.getRolle();
                String redirectUrl = "";

                if ("administrator".equalsIgnoreCase(role)) {
                    redirectUrl = "admin-dashboard";
                } else if ("geschaeftsfuehrer".equalsIgnoreCase(role)) {
                    redirectUrl = "geschaeftsfuehrer-dashboard";
                } else if ("mitarbeiter".equalsIgnoreCase(role)) {
                    redirectUrl = "mitarbeiter-dashboard";
                } else if ("mitglied".equalsIgnoreCase(role)) {
                    redirectUrl = "mitglied-dashboard";
                } else {
                    redirectUrl = "login";
                }

                UI.getCurrent().navigate(redirectUrl);
            } else {
                UI.getCurrent().navigate("login");
                Notification.show("Sie können sich jetzt einloggen.");
            }
        } catch (Exception ex) {
            Notification.show("Bitte füllen Sie alle Felder aus oder überspringen Sie.");
        }
    }
}