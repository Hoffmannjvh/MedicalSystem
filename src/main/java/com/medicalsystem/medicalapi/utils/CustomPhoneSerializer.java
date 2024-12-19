package com.medicalsystem.medicalapi.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomPhoneSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null && value.length() >= 10 && value.length() <= 11) {
            String formattedPhone;
            if (value.length() == 10) {
                // Formato para números de 10 dígitos: (XX) XXXX-XXXX
                formattedPhone = String.format("(%s) %s-%s",
                        value.substring(0, 2),       // DDD
                        value.substring(2, 6),       // Parte do meio
                        value.substring(6, 10));     // Parte final
            } else if (value.length() == 11) {
                // Formato para números de 11 dígitos: (XX) X XXXX-XXXX
                formattedPhone = String.format("(%s) %s-%s",
                        value.substring(0, 2),       // DDD
                        value.substring(2, 7),       // Parte do meio
                        value.substring(7, 11));     // Parte final
            } else {
                formattedPhone = value;  // Caso contrário, apenas usa o valor original
            }
            gen.writeString(formattedPhone);
        } else {
            gen.writeString(value);  // Se não for válido, retorna o valor original
        }
    }
}
