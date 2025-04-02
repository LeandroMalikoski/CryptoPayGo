package com.cryptopaygo.dto;

import com.cryptopaygo.enums.MovementType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MovementTypeDeserializer extends JsonDeserializer {
    @Override
    public MovementType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return MovementType.fromString(p.getValueAsString());
    }
}
