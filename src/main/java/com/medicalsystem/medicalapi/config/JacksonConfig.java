package com.medicalsystem.medicalapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.medicalsystem.medicalapi.utils.CustomDateDeserializer;
import com.medicalsystem.medicalapi.utils.CustomDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // Registra o deserializador e serializador personalizados para LocalDate
        module.addDeserializer(LocalDate.class, new CustomDateDeserializer());
        module.addSerializer(LocalDate.class, new CustomDateSerializer());

        objectMapper.registerModule(module);
        return objectMapper;
    }
}
