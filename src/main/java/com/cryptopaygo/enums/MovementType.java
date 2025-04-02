package com.cryptopaygo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MovementType {
    ENTRY, EXIT;

    public static MovementType fromString(String value) {
        return switch (value.toUpperCase()) {
            case "ENTRY" -> ENTRY;
            case "EXIT" -> EXIT;
            default -> throw new IllegalArgumentException("Valor inválido para MovementType: " + value);
        };
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
