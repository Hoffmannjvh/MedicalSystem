package com.medicalsystem.medicalapi.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomCPFSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            // Remove qualquer pontuação
            value = value.replaceAll("[^0-9]", "");

            // Se o CPF tem 11 dígitos, formata no padrão XXX.XXX.XXX-XX
            if (value.length() == 11) {
                String formattedCPF = String.format("%s.%s.%s-%s",
                        value.substring(0, 3),
                        value.substring(3, 6),
                        value.substring(6, 9),
                        value.substring(9, 11));
                gen.writeString(formattedCPF);
            } else {
                gen.writeString(value);  // Se não for válido, retorna o valor original
            }
        } else {
            gen.writeNull();  // Se for nulo, retorna null
        }
    }
}
