package com.example.demo.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Erfolgreich angemeldet")
@Route(value = "success")
public class SuccessView extends VerticalLayout {

    public SuccessView() {
        add(new H1("Erfolgreich angemeldet"));
    }
}