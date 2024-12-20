package com.medicalsystem.medicalapi.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.medicalsystem.medicalapi.exception.InvalidDateFormatException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER_WITH_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_WITHOUT_SLASH = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateTimeStr = p.getText().trim();

        try {
            if (dateTimeStr.contains("/")) {
                return LocalDateTime.parse(dateTimeStr, FORMATTER_WITH_SLASH);
            } else if (dateTimeStr.matches("\\d{14}")) { // Verifica se tem 14 dígitos para o formato sem barra
                return LocalDateTime.parse(dateTimeStr, FORMATTER_WITHOUT_SLASH);
            } else {
                throw new InvalidDateFormatException("Erro: Formato incorreto, digite a data e hora no formato dd/MM/yyyy HH:mm:ss ou ddMMyyyyHHmmss");
            }
        } catch (Exception e) {
            throw new InvalidDateFormatException("Erro: Formato incorreto, digite a data e hora no formato dd/MM/yyyy HH:mm:ss ou ddMMyyyyHHmmss");
        }
    }
}
