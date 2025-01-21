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
@Route(value = "login") // Set URL
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Paragraph pRegistrierung = new Paragraph("No account yet? Register here.");
    private final Button btnRegistrierung = new Button("Register");
    private final Button btnPassworrtVergessen = new Button("Forgot password");

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.addClassName("login-view");

        // Center the registration text
        pRegistrierung.addClassName(LumoUtility.TextAlignment.CENTER);
        pRegistrierung.getStyle().set("text-align", "center");
        pRegistrierung.getStyle().set("margin", "10px 0");

        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        createLoginForm(); // Create the login form
        stylingComponentsCss(); // Style the buttons

        setForgotPasswordButtonVisible(false); // Disable default forgot password button
        setOpened(true);

        // Navigate to password reset or registration on click
        btnPassworrtVergessen.addClickListener(e -> UI.getCurrent().navigate("passwordreset"));
        btnRegistrierung.addClickListener(e -> UI.getCurrent().navigate("register"));
    }

    // Redirect logged-in users to the home page or show login error
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            setOpened(false);
            event.forwardTo(SuccessView.class); // Routing if login was successfull
        }

        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            setErrorForLoginFailure(); // Login-Fehleranzeige aktivieren
        }
    }

    // Create the login form and bind the I18n texts
    private void createLoginForm() {
        LoginI18n i18n = createLoginI18n();
        setI18n(i18n);

        LoginForm loginFormLoginSeite = new LoginForm();
        loginFormLoginSeite.setI18n(i18n);
        loginFormLoginSeite.setAction("login");

        pRegistrierung.addClassName(LumoUtility.TextAlignment.CENTER);

        // Center the button in a layout
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.add(btnRegistrierung);

        // Add components to footer
        getFooter().add(btnPassworrtVergessen, new Hr(), pRegistrierung, buttonLayout);
    }

    private LoginI18n createLoginI18n() {
        LoginI18n i18n = LoginI18n.createDefault();

        // Load according language (en,de) based on language set in the operating
        // system.
        // Words are stored in the {i18n} messages.properties files
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("messages", UI.getCurrent().getLocale());
        } catch (MissingResourceException e) {
            // Fallback to English if no bundle / error occurs
            bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        }

        // Set Text for the login form
        LoginI18n.Form loginForm = new LoginI18n.Form();
        loginForm.setTitle(bundle.getString("login.title"));
        loginForm.setUsername(bundle.getString("login.username"));
        loginForm.setPassword(bundle.getString("login.password"));
        loginForm.setSubmit(bundle.getString("login.submit"));
        i18n.setForm(loginForm);

        // Set Header for the login form
        LoginI18n.Header header = new LoginI18n.Header();
        header.setTitle(bundle.getString("login.header.title"));
        header.setDescription(bundle.getString("login.header.description"));
        i18n.setHeader(header);

        // Configure error message
        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle(bundle.getString("login.error.title"));
        errorMessage.setMessage(bundle.getString("login.error.message"));
        i18n.setErrorMessage(errorMessage);

        return i18n;
    }

    // Add styling to the buttons
    private void stylingComponentsCss() {
        btnRegistrierung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Styling for the "Forgot password" button
        btnPassworrtVergessen.getStyle().set("display", "block");
        btnPassworrtVergessen.getStyle().set("text-align", "center");
        btnPassworrtVergessen.getStyle().set("width", "100%");
        btnPassworrtVergessen.getStyle().set("font-size", "14px");
        btnPassworrtVergessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Styling for the "Register" button
        btnRegistrierung.getStyle().set("width", "60%");
    }

    // Show error message if login fails
    private void setErrorForLoginFailure() {
        setI18n(createLoginI18n());
        setError(true);
    }
}
