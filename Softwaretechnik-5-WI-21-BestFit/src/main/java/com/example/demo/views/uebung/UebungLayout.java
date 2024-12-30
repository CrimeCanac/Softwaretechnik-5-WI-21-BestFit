package com.example.demo.views.uebung;

import java.util.*;

import com.example.demo.model.entities.Uebung;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UebungLayout extends VerticalLayout {

    private Uebung uebung;
    private List<UebungComponent> uebungComponents = new ArrayList<UebungComponent>();
    private HorizontalLayout hlHeader = new HorizontalLayout();
    private VerticalLayout vlContent = new VerticalLayout();
    private Button btnAddSatz = new Button(VaadinIcon.PLUS.create());

    public UebungLayout(Uebung uebung, int anzSaetze) {
        this.uebung = uebung;
        hlHeader.add(new H4(uebung.getName()), btnAddSatz);
        for (int i = 1; i <= anzSaetze; i++) {
            uebungComponents.add(new UebungComponent(this, uebung, i));
        }
        for (HorizontalLayout x : uebungComponents) {
            vlContent.add(x);
        }
        btnAddSatz.addClickListener(e -> {
            int satznr = uebungComponents.size() + 1;

            if (satznr > 10) {
                Notification.show("Maximal 10 Sätze erlaubt", 3000, Notification.Position.MIDDLE);
                return;
            } else if (satznr == 1) {
                vlContent.setVisible(true);
            }

            UebungComponent satz = new UebungComponent(this, uebung, satznr);
            uebungComponents.add(satz);
            vlContent.add(satz);
            setVisible(true);
        });
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("100%");
        hlHeader.setAlignItems(Alignment.BASELINE);
        add(hlHeader, vlContent);
    }

    public void deleteSatz(String strSatz) {
        for (UebungComponent x : uebungComponents) {

            if (strSatz.equals(x.getTfSatzValue_String())) {
                uebungComponents.remove(x);
                vlContent.remove(x);

                if (uebungComponents.size() == 0) {
                    vlContent.setVisible(false);
                }

                resetSatznr();
                return;
            }
        }
    }

    private void resetSatznr() {
        int satznr = 1;
        for (UebungComponent x : uebungComponents) {
            x.setTfSatzValue(satznr);
            satznr++;
        }
    }

    public int getSumSaetze() {
        return uebungComponents.size();
    }

    public int getSumWdh() {
        int sum = 0;
        for (UebungComponent x : uebungComponents) {
            sum += x.getIfWdhValue();
        }
        return sum;
    }

    public double getSumGewicht() {
        int sum = 0;
        for (UebungComponent x : uebungComponents) {
            sum += x.getNfGewichtValue();
        }
        return sum;
    }
}
