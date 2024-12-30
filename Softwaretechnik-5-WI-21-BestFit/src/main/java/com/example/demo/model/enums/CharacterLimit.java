package com.example.demo.model.enums;

// Author: Delbrin Alazo
// Created: 2024-12-07
// Last Updated: 2024-12-07
// Modified by: Delbrin Alazo
// Description: Enum für die Längenbegrenzung von Benutzereingaben

public enum CharacterLimit {
    NAME(4, 50),
    USERNAME(4, 30),
    PASSWORD(8, 30),
    ANTWORT(3, 30),
    ALTER(14, 99),
    GROESSE(100, 250),
    GEWICHT(40, 500),
    SCHRITTE(0, 100000),
    ZAPPELN(0, 24);

    private final int minLength;
    private final int maxLength;

    CharacterLimit(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
}