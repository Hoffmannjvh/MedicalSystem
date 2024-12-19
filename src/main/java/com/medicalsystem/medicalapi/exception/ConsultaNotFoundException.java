package com.medicalsystem.medicalapi.exception;


public class ConsultaNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Consulta não encontrada para o ID fornecido.";

    public ConsultaNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ConsultaNotFoundException(String customMessage) {
        super(customMessage);
    }

}
