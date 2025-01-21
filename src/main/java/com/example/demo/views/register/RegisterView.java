package com.example.demo.views.register;

// Author: Delbrin Alazo
// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: RegisterView für die Registrierung von Benutzern

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.entities.User;
import com.example.demo.service.UserService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Registrierung Pflicht")
@Route(value = "register")
public class RegisterView extends VerticalLayout {

    private final RegisterForm registerForm;
    private final RegisterFormOpt registerFormOpt;
    private final UserService userService;
    private final Tabs tabs;
    private final VerticalLayout content;
    private final Tab tab1;
    private final Tab tab2;

    public RegisterView(@Autowired UserService userService) {
        this.userService = userService;

        registerForm = new RegisterForm(this, this.userService);
        registerFormOpt = new RegisterFormOpt(this, this.userService);

        tabs = new Tabs();
        tab1 = new Tab("Schritt 1: Grundlegende Informationen");
        tab2 = new Tab("Schritt 2: Ergänzende Profilinformationen");
        tabs.add(tab1, tab2);

        content = new VerticalLayout();
        content.add(registerForm);

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            if (event.getSelectedTab().equals(tab1)) {
                content.add(registerForm);
            } else {
                content.add(registerFormOpt);
            }
        });

        add(tabs, content);
    }

    // method to get the user from the register form
    public User getUserFormRegisterForm() {
        return registerForm.getUser();
    }

    // method to get the user from the register form opt
    public void setRegisterVisible(boolean visible) {
        registerFormOpt.setVisible(visible);
    }

    // method to switch to the next tab
    public void switchToNextTab() {
        tabs.setSelectedTab(tab2);
    }
}