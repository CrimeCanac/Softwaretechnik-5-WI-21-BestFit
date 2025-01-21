package com.example.demo.views.passwordForgot;

// Author: Delbrin Alazo
// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: PasswordForgotView class for handling password reset

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.enums.Sicherheitsfrage;
import com.example.demo.model.entities.User;
import com.example.demo.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@AnonymousAllowed
@PageTitle("Passwort Zurücksetzen")
@Route("passwordreset")
public class PasswordForgotView extends VerticalLayout {

    // Layouts for the form
    FormLayout layoutPasswortForgetPage = new FormLayout();
    HorizontalLayout layoutButtonsNext = new HorizontalLayout();
    HorizontalLayout layoutButtonsUeberpruefen = new HorizontalLayout();
    HorizontalLayout layoutButtonsZuruecksetzen = new HorizontalLayout();

    // UI-Components
    H1 header = new H1("Passwort zurücksetzen");
    Paragraph pSchrittEins = new Paragraph("Schritt 1: Geben Sie ihren Benutzernamen ein");
    Paragraph pSchrittZwei = new Paragraph("Schritt 2: Geben Sie die Antwort ihrer Sicherheitsfrage ein");
    Paragraph pSchrittDrei = new Paragraph("Schritt 3: Geben Sie ihr neues Passwort ein");
    TextField tfBenutzername = new TextField("Benutzername");
    TextField tfSicherheitsfrage = new TextField("Sicherheitsfrage");
    TextField tfAntwort = new TextField("Bei der Registrierung festgelegte Antwort");
    PasswordField tfZuruecksetzen = new PasswordField("Neues Passwort");
    PasswordField tfZuruecksetzenBest = new PasswordField("Neues Passwort bestätigen");

    // Buttons
    Button buttonAbbrechen1 = new Button("Abbrechen");
    Button buttonAbbrechen2 = new Button("Abbrechen"); // Drei Buttons nötig da jeder für ein anderes Layout ist.
    Button buttonAbbrechen3 = new Button("Abbrechen");

    Button buttonNext1 = new Button("Weiter");
    Button buttonUeberpruefen = new Button("Überprüfen");
    Button buttonZurueckSetzen = new Button("Zurücksetzen");

    private String passwortStaerke = "";

    private final UserService userService;

    // Constructor: PasswordForgotView
    public PasswordForgotView(@Autowired UserService userService) {
        this.userService = userService;

        layoutPasswortForgetPage.addClassName("password-forget-form");

        stylingComponentsCss();
        setComponentsForStart();
        checkPasswords();
        setupPasswordStrengthChecker();

        // Abbrechen-Button-Listener
        buttonAbbrechen1.addClickListener(e -> buttonAbbrechenFunktion());
        buttonAbbrechen2.addClickListener(e -> buttonAbbrechenFunktion());
        buttonAbbrechen3.addClickListener(e -> buttonAbbrechenFunktion());

        layoutButtonsNext.setVisible(true);
        layoutButtonsUeberpruefen.setVisible(false);
        layoutButtonsZuruecksetzen.setVisible(false);

        // Check if the user exists
        buttonNext1.addClickListener(e -> {
            User user = userService.findByUsername(tfBenutzername.getValue());
            if (user != null) {
                tfSicherheitsfrage.setValue(Sicherheitsfrage.enumToString(user.getSicherheitsfrage()));
                setComponentsForNextStep();
            } else {
                Notification.show("Benutzername nicht gefunden.");
            }
        });

        // Check if the answer to the security question is correct
        buttonUeberpruefen.addClickListener(e -> {
            User user = userService.findByUsername(tfBenutzername.getValue());
            if (user != null && BCrypt.checkpw(tfAntwort.getValue(), user.getSicherheitsfrageAntwort())) {
                setComponentsForReset();
            } else {
                Notification.show("Antwort auf die Sicherheitsfrage ist falsch.");
            }
        });

        // Function for reseting the password
        buttonZurueckSetzen.addClickListener(e -> {
            if (tfZuruecksetzen.getValue().equals(tfZuruecksetzenBest.getValue())) {
                if (passwortStaerke.equals("Schwach")) {
                    buttonSpeichernMitSchwachenPasswortFunktion(tfZuruecksetzen.getValue());
                } else {
                    resetPassword(tfZuruecksetzen.getValue());
                }
            } else {
                Notification.show("Passwörter stimmen nicht überein.");
            }
        });
    }

    // Styling of components
    private void stylingComponentsCss() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        layoutPasswortForgetPage.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1));

        layoutPasswortForgetPage.getStyle().set("box-shadow", "0 0 10px grey");
        layoutPasswortForgetPage.getStyle().set("border-radius", "6px");
        layoutPasswortForgetPage.getStyle().set("padding", "3vh");
        layoutPasswortForgetPage.getStyle().set("padding-bottom", "20px");
        layoutPasswortForgetPage.getStyle().set("margin", "auto");

        layoutButtonsNext.add(buttonAbbrechen1, buttonNext1);
        layoutButtonsNext.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutButtonsNext.getStyle().set("padding-top", "5%");
        layoutButtonsUeberpruefen.add(buttonAbbrechen2, buttonUeberpruefen);
        layoutButtonsUeberpruefen.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutButtonsUeberpruefen.getStyle().set("padding-top", "5%");
        layoutButtonsZuruecksetzen.add(buttonAbbrechen3, buttonZurueckSetzen);
        layoutButtonsZuruecksetzen.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutButtonsZuruecksetzen.getStyle().set("padding-top", "5%");

        header.getStyle().set("text-align", "center");
        pSchrittEins.getStyle().set("text-align", "center");
        pSchrittZwei.getStyle().set("text-align", "center");
        pSchrittDrei.getStyle().set("text-align", "center");

        buttonAbbrechen1.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonAbbrechen2.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonAbbrechen3.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonNext1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonUeberpruefen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonZurueckSetzen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layoutPasswortForgetPage.add(header, pSchrittEins, pSchrittZwei, pSchrittDrei,
                tfBenutzername, tfSicherheitsfrage, tfAntwort, tfZuruecksetzen,
                tfZuruecksetzenBest);
        layoutPasswortForgetPage.add(layoutButtonsNext);
        layoutPasswortForgetPage.add(layoutButtonsUeberpruefen);
        layoutPasswortForgetPage.add(layoutButtonsZuruecksetzen);

        add(layoutPasswortForgetPage);
    }

    // Set components for the start
    private void setComponentsForStart() {
        pSchrittEins.setVisible(true);
        pSchrittZwei.setVisible(false);
        pSchrittDrei.setVisible(false);
        tfSicherheitsfrage.setVisible(false);
        tfAntwort.setVisible(false);
        tfZuruecksetzen.setVisible(false);
        tfZuruecksetzenBest.setVisible(false);
        layoutButtonsNext.setVisible(true);
        layoutButtonsUeberpruefen.setVisible(false);
        layoutButtonsNext.setVisible(true);
    }

    // Set components for the next step
    private void setComponentsForNextStep() {
        pSchrittEins.setVisible(false);
        pSchrittZwei.setVisible(true);
        pSchrittDrei.setVisible(false);
        tfBenutzername.setReadOnly(true);
        tfSicherheitsfrage.setVisible(true);
        tfSicherheitsfrage.setReadOnly(true);
        tfAntwort.setVisible(true);
        tfZuruecksetzenBest.setVisible(false);
        layoutButtonsNext.setVisible(true);
        layoutButtonsNext.setVisible(false);
        layoutButtonsUeberpruefen.setVisible(true);
        layoutButtonsNext.setVisible(false);
    }

    // Set components for the password reset
    private void setComponentsForReset() {
        pSchrittEins.setVisible(false);
        pSchrittZwei.setVisible(false);
        pSchrittDrei.setVisible(true);
        tfBenutzername.setVisible(false);
        layoutButtonsNext.setVisible(false);
        tfSicherheitsfrage.setVisible(false);
        tfAntwort.setVisible(false);
        layoutButtonsUeberpruefen.setVisible(false);
        tfZuruecksetzen.setVisible(true);
        tfZuruecksetzenBest.setVisible(true);
        layoutButtonsZuruecksetzen.setVisible(true);
    }

    // function for the cancel button
    private void buttonAbbrechenFunktion() {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setModal(true);
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);

        Span spanAbbrechen = new Span("Sind Sie sicher, dass Sie abbrechen möchten?");
        Button buttonBestaetigenBestaetigung = new Button("Ja", event -> {
            UI.getCurrent().navigate("login");
            confirmationDialog.close();
        });

        buttonBestaetigenBestaetigung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnAbbrechenBestaetigung = new Button("Nein", event -> confirmationDialog.close());
        btnAbbrechenBestaetigung.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // create a horizontal layout for the buttons and center
        HorizontalLayout buttonLayout = new HorizontalLayout(btnAbbrechenBestaetigung, buttonBestaetigenBestaetigung);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);

        // create a vertical layout for the dialog content
        VerticalLayout dialogLayout = new VerticalLayout(spanAbbrechen, buttonLayout);
        dialogLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        confirmationDialog.add(dialogLayout);

        confirmationDialog.open();
    }

    // check if both provided passwords match
    private void checkPasswords() {
        tfZuruecksetzenBest.setValueChangeMode(ValueChangeMode.LAZY);

        tfZuruecksetzenBest.addValueChangeListener(event -> {
            if (!tfZuruecksetzen.getValue().equals(tfZuruecksetzenBest.getValue())) {
                tfZuruecksetzenBest.setInvalid(true);
                tfZuruecksetzenBest.setErrorMessage("Die Passwörter stimmen nicht überein.");
            } else {
                tfZuruecksetzenBest.setInvalid(false);
            }
        });
    }

    // function for checking the password strength
    private void setupPasswordStrengthChecker() {
        Icon checkIcon = VaadinIcon.CHECK.create();
        checkIcon.setVisible(false);
        checkIcon.getStyle().set("color", "var(--lumo-success-color)");
        tfZuruecksetzen.setSuffixComponent(checkIcon);

        Div passwordStrength = new Div();
        Span passwordStrengthText = new Span();
        passwordStrength.add(new com.vaadin.flow.component.Text("Stärke des Passwortes: "), passwordStrengthText);
        tfZuruecksetzen.setHelperComponent(passwordStrength);

        tfZuruecksetzen.setValueChangeMode(ValueChangeMode.EAGER);

        tfZuruecksetzen.addValueChangeListener(event -> {
            String password = event.getValue();
            String strength = evaluatePasswordStrength(password);
            passwortStaerke = strength;
            passwordStrengthText.setText(strength);

            if (strength.equals("Stark")) {
                passwordStrengthText.getStyle().set("color", "green");
                checkIcon.setVisible(true);
            } else {
                if (strength.equals("Mittel"))
                    passwordStrengthText.getStyle().set("color", "orange");
                else {
                    passwordStrengthText.getStyle().set("color", "red");
                }
                checkIcon.setVisible(false);
            }
        });
    }

    // function for calculating the password strength
    private String evaluatePasswordStrength(String password) {
        if (password.length() < 6) {
            return "Schwach";
        }

        int strengthPoints = 0;

        if (password.length() >= 10) {
            strengthPoints++;
        }

        // Großbuchstaben
        if (password.matches(".*[A-Z].*")) {
            strengthPoints++;
        }

        // Kleinbuchstaben
        if (password.matches(".*[a-z].*")) {
            strengthPoints++;
        }

        // Zahlen
        if (password.matches(".*\\d.*")) {
            strengthPoints++;
        }

        // Sonderzeichen
        if (password.matches(".*[!@#$%^&*()-+].*")) {
            strengthPoints++;
        }

        if (strengthPoints >= 4) {
            return "Stark";
        } else if (strengthPoints >= 2) {
            return "Mittel";
        } else {
            return "Schwach";
        }
    }

    // confirm that the user wants to use a weak password
    private void buttonSpeichernMitSchwachenPasswortFunktion(String passwort) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setModal(true);
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);

        Span spanAbbrechen = new Span("Sind Sie sicher, dass Sie ein schwaches Passwort benutzen wollen?");

        Button buttonBestaetigenBestaetigung = new Button("Ja", event -> {
            resetPassword(passwort);
            confirmationDialog.close();
        });
        buttonBestaetigenBestaetigung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnAbbrechenBestaetigung = new Button("Nein", event -> confirmationDialog.close());
        btnAbbrechenBestaetigung.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // create a horizontal layout for the buttons and center
        HorizontalLayout buttonLayout = new HorizontalLayout(btnAbbrechenBestaetigung,
                buttonBestaetigenBestaetigung);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);

        // create a vertical layout for the dialog content
        VerticalLayout dialogLayout = new VerticalLayout(spanAbbrechen, buttonLayout);
        dialogLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        confirmationDialog.add(dialogLayout);

        confirmationDialog.open();
    }

    // reset the password
    private void resetPassword(String passwort) {
        User user = userService.findByUsername(tfBenutzername.getValue());
        if (user != null) {
            user.setPasswort(new BCryptPasswordEncoder().encode(passwort));
            userService.update(user);
            Notification.show("Passwort erfolgreich zurückgesetzt.");
            UI.getCurrent().navigate("login");
        } else {
            Notification.show("Benutzername nicht gefunden.");
        }
    }
}