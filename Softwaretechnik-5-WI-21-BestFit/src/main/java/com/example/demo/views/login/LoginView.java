package com.example.demo.views.login;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: LoginView 

import com.example.demo.security.AuthenticatedUser;
import com.example.demo.views.SuccessView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@AnonymousAllowed
@PageTitle("Login || BestFit") // Tab Name
@Route(value = "login") // Setzen der URL
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Paragraph pRegistrierung = new Paragraph("Noch keinen Account? Registrieren Sie sich hier.");
    private final Button btnRegistrierung = new Button("Registrieren");
    private final Button btnPassworrtVergessen = new Button("Passwort vergessen");

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.addClassName("login-view");

        // Zentriert den Registrierungstext
        pRegistrierung.addClassName(LumoUtility.TextAlignment.CENTER);
        pRegistrierung.getStyle().set("text-align", "center");
        pRegistrierung.getStyle().set("margin", "10px 0");

        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        createLoginForm(); // Erstellt das Login-Formular
        stylingComponentsCss(); // Styling der Buttons

        setForgotPasswordButtonVisible(false); // Deaktiviert Standard-Button für Passwort vergessen
        setOpened(true);

        // Navigiert zu Passwort-Reset oder Registrierung bei Klick
        btnPassworrtVergessen.addClickListener(e -> UI.getCurrent().navigate("passwordreset"));
        btnRegistrierung.addClickListener(e -> UI.getCurrent().navigate("register"));
    }

    // Leitet eingeloggte Benutzer zur Startseite weiter oder zeigt Login-Fehler an
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            setOpened(false);
            event.forwardTo(SuccessView.class); // Routing sollte Login erfolgreich sein
        }

        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            setErrorForLoginFailure(); // Login-Fehleranzeige aktivieren
        }
    }

    // Erstellt das Login-Formular und bindet die I18n-Texte ein
    private void createLoginForm() {
        LoginI18n i18n = createLoginI18n();
        setI18n(i18n);

        LoginForm loginFormLoginSeite = new LoginForm();
        loginFormLoginSeite.setI18n(i18n);
        loginFormLoginSeite.setAction("login");

        pRegistrierung.addClassName(LumoUtility.TextAlignment.CENTER);

        // Zentriert den Button in einem Layout
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.add(btnRegistrierung);

        // Fügt die zusätzlichen Komponenten in den Footer ein
        getFooter().add(btnPassworrtVergessen, new Hr(), pRegistrierung, buttonLayout);
    }

    private LoginI18n createLoginI18n() {
        LoginI18n i18n = LoginI18n.createDefault();

        // Lädt die korrekte Sprachdatei (en,de) basierend auf der Sprache die im
        // Betriebssystem eingestellt ist
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("messages", UI.getCurrent().getLocale());
        } catch (MissingResourceException e) {
            // Fallback auf Englisch, falls kein Bundle / Fehler auftritt
            bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        }

        // Setzt die Texte für das Formular
        LoginI18n.Form loginForm = new LoginI18n.Form();
        loginForm.setTitle(bundle.getString("login.title"));
        loginForm.setUsername(bundle.getString("login.username"));
        loginForm.setPassword(bundle.getString("login.password"));
        loginForm.setSubmit(bundle.getString("login.submit"));
        i18n.setForm(loginForm);

        // Setzt die Kopfzeile
        LoginI18n.Header header = new LoginI18n.Header();
        header.setTitle(bundle.getString("login.header.title"));
        header.setDescription(bundle.getString("login.header.description"));
        i18n.setHeader(header);

        // Konfiguriert die Fehlermeldung
        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle(bundle.getString("login.error.title"));
        errorMessage.setMessage(bundle.getString("login.error.message"));
        i18n.setErrorMessage(errorMessage);

        return i18n;
    }

    // Fügt Styling für Buttons und Layouts hinzu
    private void stylingComponentsCss() {
        btnRegistrierung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Styling für "Passwort vergessen"-Button
        btnPassworrtVergessen.getStyle().set("display", "block");
        btnPassworrtVergessen.getStyle().set("text-align", "center");
        btnPassworrtVergessen.getStyle().set("width", "100%");
        btnPassworrtVergessen.getStyle().set("font-size", "14px");
        btnPassworrtVergessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Styling für den Registrierungsbutton
        btnRegistrierung.getStyle().set("width", "60%");
    }

    // Zeigt eine Fehlermeldung bei fehlerhaftem Login an
    private void setErrorForLoginFailure() {
        setI18n(createLoginI18n());
        setError(true);
    }
}
