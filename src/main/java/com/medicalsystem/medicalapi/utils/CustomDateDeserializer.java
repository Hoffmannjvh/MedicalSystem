package com.medicalsystem.medicalapi.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.medicalsystem.medicalapi.exception.InvalidDateFormatException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER_WITH_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_WITHOUT_SLASH = DateTimeFormatter.ofPattern("ddMMyyyy");


    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText().trim();

        try {
            if (dateStr.contains("/")) {
                return LocalDate.parse(dateStr, FORMATTER_WITH_SLASH);
            } else if (dateStr.matches("\\d{8}")) { // Valida se tem 8 dígitos para evitar formatos inválidos
                return LocalDate.parse(dateStr, FORMATTER_WITHOUT_SLASH);
            } else {
                throw new InvalidDateFormatException("Erro: Formato incorreto, digite a data no formato dd/MM/yyyy ou ddMMyyyy");
            }
        } catch (Exception e) {
            throw new InvalidDateFormatException("Erro: Formato incorreto, digite a data no formato dd/MM/yyyy ou ddMMyyyy");
        }
    }
}
