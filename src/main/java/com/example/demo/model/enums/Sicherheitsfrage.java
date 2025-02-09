package com.example.demo.model.enums;

// Author: Delbrin Alazo

// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: Enum for Security Question in the application

public enum Sicherheitsfrage {

    geburtsort,
    mutter_maiden_name,
    lieblingslehrer,
    lieblingsbuch;

    public static String enumToString(Sicherheitsfrage e) {
        switch (e) {
            case geburtsort:
                return "In welcher Stadt sind Sie geboren?";
            case mutter_maiden_name:
                return "Wie lautet der Geburtsname Ihrer Mutter?";
            case lieblingslehrer:
                return "Wie hieß Ihr lieblingslehrer?";
            case lieblingsbuch:
                return "Was ist Ihr lieblingsbuch?";
            default:
                return "Unbekannt";
        }
    }

    public static Sicherheitsfrage stringToEnum(String s) {
        switch (s) {
            case "In welcher Stadt sind Sie geboren?":
                return geburtsort;
            case "Wie lautet der Geburtsname Ihrer Mutter?":
                return mutter_maiden_name;
            case "Wie hieß Ihr lieblingslehrer?":
                return lieblingslehrer;
            case "Was ist Ihr lieblingsbuch?":
                return lieblingsbuch;
            default:
                throw new IllegalArgumentException("Unbekannt");
        }
    }

}