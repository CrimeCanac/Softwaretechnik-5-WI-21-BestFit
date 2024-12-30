package com.example.demo.model.enums;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: Enum für Sicherheitsfragen

public enum Sicherheitsfrage {

    GEBURTSORT,
    MUTTER_MAIDEN_NAME,
    LIEBLINGSLEHRER,
    LIEBLINGSBUCH;

    public static String enumToString(Sicherheitsfrage e) {
        switch (e) {
            case GEBURTSORT:
                return "In welcher Stadt sind Sie geboren?";
            case MUTTER_MAIDEN_NAME:
                return "Wie lautet der Geburtsname Ihrer Mutter?";
            case LIEBLINGSLEHRER:
                return "Wie hieß Ihr Lieblingslehrer?";
            case LIEBLINGSBUCH:
                return "Was ist Ihr Lieblingsbuch?";
            default:
                return "Unbekannt";
        }
    }

    public static Sicherheitsfrage stringToEnum(String s) {
        switch (s) {
            case "In welcher Stadt sind Sie geboren?":
                return GEBURTSORT;
            case "Wie lautet der Geburtsname Ihrer Mutter?":
                return MUTTER_MAIDEN_NAME;
            case "Wie hieß Ihr Lieblingslehrer?":
                return LIEBLINGSLEHRER;
            case "Was ist Ihr Lieblingsbuch?":
                return LIEBLINGSBUCH;
            default:
                throw new IllegalArgumentException("Unbekannt");
        }
    }

}