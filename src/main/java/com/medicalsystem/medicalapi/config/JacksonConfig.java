package com.medicalsystem.medicalapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.medicalsystem.medicalapi.utils.CustomDateDeserializer;
import com.medicalsystem.medicalapi.utils.CustomDateSerializer;
import com.medicalsystem.medicalapi.utils.CustomDateTimeDeserializer;
import com.medicalsystem.medicalapi.utils.CustomDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // Registra os deserializadores e serializadores personalizados
        module.addDeserializer(LocalDate.class, new CustomDateDeserializer());
        module.addDeserializer(LocalDateTime.class, new CustomDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new CustomDateSerializer());
        module.addSerializer(LocalDateTime.class, new CustomDateTimeSerializer());

        objectMapper.registerModule(module);
        return objectMapper;
    }
}
