package com.medicalsystem.medicalapi.exception;

public class MedicoNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Médico não encontrado para o ID fornecido..";

    public MedicoNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MedicoNotFoundException(String customMessage) {
        super(customMessage);
    }

}
