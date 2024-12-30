package com.example.demo.views.uebung;

import com.example.demo.model.entities.Uebung;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class UebungComponent extends HorizontalLayout{

    private IntegerField ifWdh = new IntegerField();
    private NumberField nfGewicht = new NumberField();
    private TextField tfSatz = new TextField();
    private Button btnDelete = new Button("", VaadinIcon.TRASH.create());

    public UebungComponent(UebungLayout parent, Uebung uebung, int satznr) {

        tfSatz.setValue(satznr + ".");
        tfSatz.setLabel("Satz");
        tfSatz.setReadOnly(true);
        tfSatz.setWidth("60px");
        ifWdh.setLabel("Wdh");
        ifWdh.setMin(0);
        ifWdh.setMax(100);
        ifWdh.setWidth("100px");
        nfGewicht.setLabel("Gewicht");
        nfGewicht.setMin(0);
        nfGewicht.setMax(1000);
        nfGewicht.setWidth("100px");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> {
            parent.deleteSatz(tfSatz.getValue());
        });
        setAlignItems(Alignment.BASELINE);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidth("100%");
        add(tfSatz, ifWdh, nfGewicht, btnDelete);
    }

    public void setIfWdhValue(int wdh) {
        ifWdh.setValue(wdh);
    }

    public int getIfWdhValue() {
        if(ifWdh.getValue() == null) {
            return 0;
        } else {
            return ifWdh.getValue();
        }
    }

    public void setNfGewichtValue(double gewicht) {
        nfGewicht.setValue(gewicht);
    }

    public double getNfGewichtValue() {
        if(nfGewicht.getValue() == null) {
            return 0;
        } else {
            return nfGewicht.getValue();
        }
    }

    public void setTfSatzValue(int satznr) {
        tfSatz.setValue(satznr + ".");
    }

    public String getTfSatzValue_String() {
        return tfSatz.getValue();
    }

    public int getTfSatzValue_int() {
        String value = tfSatz.getValue();
        if (value.matches("\\d+\\.?")) {
            int satz = Integer.parseInt(value.replace(".", ""));
            if (satz > 0) {
                return satz;
            } else {
                return 0;
            }

        } else {
            throw new NumberFormatException("Ungültiger Wert: " + value);
        }
    }
}
