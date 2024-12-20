package com.medicalsystem.medicalapi.exception;

public class PacienteException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Houve um erro ao tentar salvar. Por favor, tente novamente ou entre em contato com o suporte.";

    public PacienteException() {
        super(DEFAULT_MESSAGE);
    }

    public PacienteException(String customMessage) {
        super(customMessage);
    }

}
