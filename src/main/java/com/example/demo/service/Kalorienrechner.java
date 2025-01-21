package com.example.demo.service;
// Author: Delbrin Alazo

// Created: 2025-01-15
// Modified by: Delbrin Alazo
// Description: Mitglied Dashboard mit Kalorien- und Proteinrechner

public class Kalorienrechner {

    public static double berechneGrundumsatz(double gewicht, double groesse, double alter) {
        return 10 * gewicht + 6.25 * groesse - 5 * alter + 5;
    }

    public static double berechneKalorienbedarf(double grundumsatz, int aktivitaetslevel) {
        double pal = 1.0;
        switch (aktivitaetslevel) {
            case 1:
                pal = 1.3;
                break;
            case 2:
                pal = 1.45;
                break;
            case 3:
                pal = 1.65;
                break;
            case 4:
                pal = 1.9;
                break;
            case 5:
                pal = 2.35;
                break;
        }
        return Math.round(grundumsatz * pal);
    }

    public static double berechneProteinbedarf(double gewicht) {
        return gewicht * 1.8;
    }
}