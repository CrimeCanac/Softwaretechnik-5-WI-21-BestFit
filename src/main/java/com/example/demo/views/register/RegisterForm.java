package com.example.demo.views.register;

// Author: Delbrin Alazo
// Created: 2024-12-07
// Last Updated: 2025-01-31
// Modified by: Delbrin Alazo
// Description: Register form for user registration main registration form

import com.example.demo.model.entities.User;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.model.enums.Role;
import com.example.demo.model.enums.Sicherheitsfrage;
import com.example.demo.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Optional;


public class RegisterForm extends VerticalLayout {

    FormLayout layoutRegisterPage = new FormLayout();
    HorizontalLayout layoutButtons = new HorizontalLayout();

    H1 header = new H1("Registrierung");
    Paragraph pBeschreibung = new Paragraph("Bitte füllen Sie die folgenden Felder aus.");
    TextField tfBenutzername = new TextField("Benutzername");
    TextField tfVorname = new TextField("Vorname");
    TextField tfNachname = new TextField("Nachname");
    PasswordField tfPasswort = new PasswordField("Passwort");
    PasswordField tfPasswortBestaetigen = new PasswordField("Passwort bestätigen");
    ComboBox<Sicherheitsfrage> cbSicherheitsfrage = new ComboBox<>("Sicherheitsfrage");
    TextField tfAntwort = new TextField("Antwort");

    ComboBox<Role> cbRole = new ComboBox<>("Rolle auswählen");
    PasswordField pfMasterPassword = new PasswordField("Masterpasswort");
    Checkbox cbPremium = new Checkbox("Premium-Mitgliedschaft");

    Button btnAbbrechen = new Button("Abbrechen");
    Button btnWeiter = new Button("Weiter");

    ProgressBar pbRegistrierung = new ProgressBar();

    private String passwortStaerke = "";

    private final UserService userService;
    private final RegisterView registerView;
    private final AuthenticatedUser authenticatedUser;

    private User user;

    public RegisterForm(RegisterView registerView, UserService userService, AuthenticatedUser authenticatedUser) {

        layoutRegisterPage.addClassName("register-form");
        this.registerView = registerView;
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;

        stylingComponentsCss();
        setupPasswordStrengthChecker();
        checkPasswords();

        // if role is not mitglied, show master password field
        pfMasterPassword.setVisible(false);
        cbPremium.setVisible(false);
        cbRole.setItems(Role.values());
        cbRole.addValueChangeListener(event -> {
            Role selectedRole = event.getValue();
            if (selectedRole != null && selectedRole != Role.mitglied) {
                pfMasterPassword.setVisible(true);
                cbPremium.setVisible(false);
            } else {
                pfMasterPassword.setVisible(false);
                cbPremium.setVisible(true);
            }
        });

        btnAbbrechen.addClickListener(e -> {
            btnAbbrechenFunktion();
        });

        btnWeiter.addClickListener(e -> {
            btnWeiterFunktion();
        });
    }

    // Styling of components
    private void stylingComponentsCss() {

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Layout for the registration form
        layoutRegisterPage.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));

        layoutRegisterPage.getStyle().set("box-shadow", "0 0 10px grey");
        layoutRegisterPage.getStyle().set("border-radius", "6px");
        layoutRegisterPage.getStyle().set("padding", "3vh");
        layoutRegisterPage.getStyle().set("margin", "auto");
        layoutRegisterPage.getStyle().set("padding-bottom", "20px");

        btnAbbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnWeiter.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layoutButtons.add(btnAbbrechen, btnWeiter);
        layoutButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutButtons.getStyle().set("padding-top", "5%");

        // Styling of the components
        header.setWidth("47vh");
        header.getStyle().set("padding-top", "3%");
        header.getStyle().set("text-align", "center");

        pBeschreibung.setWidth("50%");
        pBeschreibung.getStyle().set("text-align", "center");

        pbRegistrierung.setValue(0.5);
        pbRegistrierung.setWidth("50%");

        tfBenutzername.setMinLength(4);
        tfBenutzername.setMaxLength(30);
        tfBenutzername.setHelperText("Bitte 4 bis 30 Zeichen eingeben");
        tfBenutzername.getStyle().set("padding-top", "1vh");
        tfBenutzername.setRequired(true);

        tfVorname.getStyle().set("padding-top", "1vh");
        tfVorname.setMinLength(2);
        tfVorname.setMaxLength(50);
        tfVorname.setHelperText("Bitte 2 bis 50 Zeichen eingeben");
        tfVorname.setRequired(true);

        tfNachname.getStyle().set("padding-top", "1vh");
        tfNachname.setMinLength(2);
        tfNachname.setMaxLength(50);
        tfNachname.setHelperText("Bitte 2 bis 50 Zeichen eingeben");
        tfNachname.setRequired(true);

        tfPasswort.getStyle().set("padding-top", "1vh");
        tfPasswort.setMinLength(8);
        tfPasswort.setMaxLength(30);
        tfPasswort.setRequired(true);

        Div passwordStrength = new Div();
        Span passwordStrengthText = new Span();
        passwordStrength.add(new com.vaadin.flow.component.Text("Stärke des Passwortes: "), passwordStrengthText);
        tfPasswort.setHelperComponent(passwordStrength);

        tfPasswortBestaetigen.getStyle().set("padding-top", "1vh");
        tfPasswortBestaetigen.setRequired(true);

        cbSicherheitsfrage.getStyle().set("padding-top", "1vh");
        cbSicherheitsfrage.setRequired(true);

        tfAntwort.setMinLength(3);
        tfAntwort.setMaxLength(50);
        tfAntwort.getStyle().set("padding-top", "1vh");
        tfAntwort.setRequired(true);
        tfAntwort.setHelperText("Bitte min 3 Zeichen eingeben");

        cbSicherheitsfrage.setPlaceholder("Wählen Sie eine Sicherheitsfrage aus");
        cbSicherheitsfrage.setItems(Sicherheitsfrage.values());
        cbSicherheitsfrage.setItemLabelGenerator(Sicherheitsfrage::enumToString);

        cbRole.getStyle().set("padding-top", "1vh");
        cbRole.setRequired(true);

        pfMasterPassword.getStyle().set("padding-top", "1vh");

        layoutRegisterPage.add(pbRegistrierung, 2);
        layoutRegisterPage.add(header, 2);
        layoutRegisterPage.add(pBeschreibung, 2);
        layoutRegisterPage.add(tfVorname, tfNachname);
        layoutRegisterPage.add(tfBenutzername, 2);
        layoutRegisterPage.add(tfPasswort, tfPasswortBestaetigen);
        layoutRegisterPage.add(cbSicherheitsfrage, tfAntwort);
        layoutRegisterPage.add(cbRole, pfMasterPassword);
        layoutRegisterPage.add(cbPremium, 2);
        layoutRegisterPage.add(layoutButtons, 2);

        add(layoutRegisterPage);
    }

    // function for the cancel button
    private void btnAbbrechenFunktion() {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setModal(true);
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);

        Span spanAbbrechen = new Span("Sind Sie sicher, dass Sie abbrechen möchten?");
        Button buttonBestaetigenBestaetigung = new Button("Ja", event -> {
            Optional<User> currentUser = authenticatedUser.get();
            if (currentUser.isPresent()) {
                String role = currentUser.get().getRolle();
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
            }
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

    // Function for the continue button
    private void btnWeiterFunktion() {
        String passwort = tfPasswort.getValue();

        // check if all fields are filled
        if (tfBenutzername.getValue().isEmpty() || tfPasswort.getValue().isEmpty()
                || tfPasswortBestaetigen.getValue().isEmpty()
                || cbSicherheitsfrage.getValue() == null || tfAntwort.getValue().isEmpty()
                || cbRole.getValue() == null) {
            Notification.show("Bitte füllen Sie alle Felder aus.");
        } // check if the username already exists
        else if (userService.userExists(tfBenutzername.getValue())) {
            Notification.show("Benutzername bereits vergeben. Bitte geben Sie einen anderen an", 1500,
                    Notification.Position.MIDDLE);
        }
        // check if the username contains spaces
        else if (tfBenutzername.getValue().contains(" ") || tfPasswort.getValue().contains(" ")) {
            Notification.show("Leerzeichen sind nicht erlaubt.");
        }
        // check if the username contains "user", "admin", "test" or "root"
        else if (tfBenutzername.getValue().toLowerCase().contains("user")
                || tfBenutzername.getValue().toLowerCase().contains("master")
                || tfBenutzername.getValue().toLowerCase().contains("test")
                || tfBenutzername.getValue().toLowerCase().contains("root")
                || tfBenutzername.getValue().toLowerCase().contains("admin")) {
            Notification.show("Der Benutzername darf nicht 'user', 'master', 'admin', 'test' oder 'root' enthalten.");
        }

        // Check if the role is not mitglied and validate the master password
        else if (!cbRole.getValue().equals(Role.mitglied)) {
            if (pfMasterPassword.isEmpty()) {
                Notification.show("Bitte geben Sie das Master-Passwort ein.");
            } else if (cbRole.getValue().equals(Role.administrator)
                    || cbRole.getValue().equals(Role.geschaeftsfuehrer)) {
                if (!userService.verifyMasterPassword(pfMasterPassword.getValue())) {
                    Notification.show("Das Master-Passwort ist falsch.");
                } else {
                    validateAndCreateUser(passwort);
                }
            } else if (cbRole.getValue().equals(Role.mitarbeiter)) {
                if (!userService.verifyAdminOrGeschaeftsfuehrerPassword(pfMasterPassword.getValue())) {
                    Notification.show("Das Passwort eines Geschäftsführers oder Admins ist falsch.");
                } else {
                    validateAndCreateUser(passwort);
                }
            }
        } else {
            validateAndCreateUser(passwort);
        }
    }

    private void validateAndCreateUser(String passwort) {
        if (passwort.length() < 8 && !passwort.isEmpty() || passwort.length() > 30) {
            tfPasswort.setErrorMessage("Passwort muss mindestens 8 und maximal 30 Zeichen lang sein");
            tfPasswort.setInvalid(true);
        } else {
            tfPasswort.setInvalid(false);
            // Check if the password is strong enough, if not ask if you want to continue
            // with a weak password
            if (passwortStaerke.equals("Schwach")) {
                passwort = tfPasswort.getValue();
                buttonSpeichernMitSchwachenPasswortFunktion(passwort);
            } else {
                createUser(passwort);
                registerView.switchToNextTab();
            }
        }
    }

    // Function for the password strength checker
    private void setupPasswordStrengthChecker() {
        Icon checkIcon = VaadinIcon.CHECK.create();
        checkIcon.setVisible(false);
        checkIcon.getStyle().set("color", "var(--lumo-success-color)");
        tfPasswort.setSuffixComponent(checkIcon);

        Div passwordStrength = new Div();
        Span passwordStrengthText = new Span();
        passwordStrength.add(new com.vaadin.flow.component.Text("Stärke des Passwortes: "), passwordStrengthText);
        tfPasswort.setHelperComponent(passwordStrength);

        tfPasswort.setValueChangeMode(ValueChangeMode.EAGER);

        tfPasswort.addValueChangeListener(event -> {
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

    // Function for the password strength calculation
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

    // check if the passwords match
    private void checkPasswords() {
        tfPasswortBestaetigen.setValueChangeMode(ValueChangeMode.LAZY);

        tfPasswortBestaetigen.addValueChangeListener(event -> {
            if (!tfPasswort.getValue().equals(tfPasswortBestaetigen.getValue())) {
                tfPasswortBestaetigen.setInvalid(true);
                tfPasswortBestaetigen.setErrorMessage("Die Passwörter stimmen nicht überein.");
            } else {
                tfPasswortBestaetigen.setInvalid(false);
            }
        });
    }

    // confirm if you want to continue with a weak password
    private void buttonSpeichernMitSchwachenPasswortFunktion(String passwort) {

        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setModal(true);
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);

        Span spanAbbrechen = new Span("Sind Sie sicher, dass Sie ein schwaches Passwort benutzen wollen?");

        Button buttonBestaetigenBestaetigung = new Button("Ja", event -> {
            createUser(passwort);
            registerView.switchToNextTab();
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

    // create the user
    private void createUser(String passwort) {
        passwort = tfPasswort.getValue();
        String hashedPasswort = BCrypt.hashpw(passwort, BCrypt.gensalt());
        String hashedSicherheitsfrageAntwort = BCrypt.hashpw(tfAntwort.getValue(), BCrypt.gensalt());

        user = new User();

        if (userService.userExists(tfBenutzername.getValue())) {
            Notification.show("Benutzername bereits vergeben.");
            tfBenutzername.setInvalid(true);
            return;
        }

        user.setVorname(tfVorname.getValue());
        user.setNachname(tfNachname.getValue());
        user.setBenutzername(tfBenutzername.getValue());
        user.setPasswort(hashedPasswort);
        user.setSicherheitsfrage(cbSicherheitsfrage.getValue());
        user.setSicherheitsfrageAntwort(hashedSicherheitsfrageAntwort);
        user.setRolle(cbRole.getValue().name());
        user.setIsPremium(cbPremium.getValue());

        userService.update(user);
    }

    public String getPasswortStaerke() {
        return passwortStaerke;
    }

    public void setPasswortStaerke(String passwortStaerke) {
        this.passwortStaerke = passwortStaerke;
    }

    public User getUser() {
        return user;
    }
}