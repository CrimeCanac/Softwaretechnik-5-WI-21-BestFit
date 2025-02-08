package com.example.demo.views.training;

//Author: Ömer Yalcinkaya

import com.example.demo.model.entities.Trainingshistorie;
import com.example.demo.service.TrainingshistorieService;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class StopuhrView extends VerticalLayout {
    private H3 zeit = new H3("00:00:00");
    private Button btnStart = new Button("Start", VaadinIcon.PLAY.create());
    private Button btnPause = new Button("Stop", VaadinIcon.PAUSE.create());
    private Button btnWeiter = new Button("Weiter", VaadinIcon.PLAY.create());
    private Button btnReset = new Button("Reset", VaadinIcon.REFRESH.create());
    private boolean hasStarted = false;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public StopuhrView() {
        btnStart.addClickListener(e -> start());
        btnStart.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        btnPause.addClickListener(e -> stop());
        btnPause.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnWeiter.addClickListener(e -> weiter());
        btnWeiter.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        btnReset.addClickListener(e -> reset());
        btnReset.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnPause.setVisible(false);
        btnWeiter.setVisible(false);
        btnReset.setVisible(false);
        setAlignItems(Alignment.CENTER);
        add(zeit, new HorizontalLayout(btnStart, btnPause, btnWeiter, btnReset));
    }

    private void start() {
        if (!isRunning && !hasStarted) {
            hasStarted = true;
            isRunning = true;
            getElement().executeJs(
                "this.startTime = Date.now();" + // Initialisiere die Startzeit
                "if (this.timer) clearInterval(this.timer);" + // Stelle sicher, dass kein alter Timer läuft
                "this.timer = setInterval(() => {" +
                "  const elapsed = Date.now() - this.startTime;" +
                "  const seconds = Math.floor((elapsed / 1000) % 60);" +
                "  const minutes = Math.floor((elapsed / 1000 / 60) % 60);" +
                "  const hours = Math.floor(elapsed / 1000 / 60 / 60);" +
                "  this.querySelector('h3').innerText = hours.toString().padStart(2, '0') + ':' +" +
                "    minutes.toString().padStart(2, '0') + ':' + seconds.toString().padStart(2, '0');" +
                "}, 1000);"
            );
            btnStart.setVisible(false);
            btnPause.setVisible(true);
            btnReset.setVisible(true);
        }    
    }

    public void stop() {
        if (isRunning && hasStarted) {
            isRunning = false;
            isPaused = true;
            getElement().executeJs(
                "clearInterval(this.timer);" + 
                "this.pausedTime = Date.now() - this.startTime;" // Speichere die verstrichene Zeit
            );
            btnWeiter.setVisible(true);
            btnPause.setVisible(false);
            btnReset.setVisible(true);
        }
    }

    private void weiter() {
        if (!isRunning && hasStarted && isPaused) {
            isRunning = true;
            getElement().executeJs(
                "this.startTime = Date.now() - this.pausedTime;" + // Berechnung der neuen Startzeit
                "this.timer = setInterval(() => {" +
                "  const elapsed = Date.now() - this.startTime;" +
                "  const seconds = Math.floor((elapsed / 1000) % 60);" +
                "  const minutes = Math.floor((elapsed / 1000 / 60) % 60);" +
                "  const hours = Math.floor(elapsed / 1000 / 60 / 60);" +
                "  this.querySelector('h3').innerText = hours.toString().padStart(2, '0') + ':' +" +
                "    minutes.toString().padStart(2, '0') + ':' + seconds.toString().padStart(2, '0');" +
                "}, 1000);"
            );
            btnWeiter.setVisible(false);
            btnPause.setVisible(true);
            btnReset.setVisible(true);
        }
    }

    private void reset() {
        isRunning = false;
        hasStarted = false;
        isPaused = false;
        getElement().executeJs(
            "clearInterval(this.timer);" +
            "this.startTime = null;" +
            "this.pausedTime = null;" +
            "this.querySelector('h3').innerText = '00:00:00';"
        );
        btnStart.setVisible(true);
        btnPause.setVisible(false);
        btnWeiter.setVisible(false);
        btnReset.setVisible(false);
    }

    // Diese Methode wird verwendet, um die Zeit zu synchronisieren, wenn das Training beendet wird
    public void syncTimeAndUpdateBackend(Trainingshistorie th, TrainingshistorieService trainingshistorieService) {
        getElement().executeJs("return this.querySelector('h3').innerText;")
            .then(String.class, time -> {
                getElement().callJsFunction("$server.updateTime", time); // Zeit an das Backend übergeben
                System.out.println("Zeit an das Backend gesendet: " + time);
                zeit.setText(time);
                th.setDauerByString(time);
                trainingshistorieService.saveTrainingshistorie(th);
            });
        
    }

    public String getTimerCurrentTime() {
        return zeit.getText(); // Gib den aktuellen Wert zurück
    }

    public int getTimerLengthInSeconds() {
        String[] time = getTimerCurrentTime().split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int seconds = Integer.parseInt(time[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }

    public int getTimerHours() {
        return Integer.parseInt(getTimerCurrentTime().split(":")[0]);
    }

    public int getTimerMinutes() {
        return Integer.parseInt(getTimerCurrentTime().split(":")[1]);
    }

    public int getTimerSeconds() {
        return Integer.parseInt(getTimerCurrentTime().split(":")[2]);
    }
}