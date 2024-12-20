package com.medicalsystem.medicalapi.exception;

public class MedicoException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Houve um erro ao tentar salvar. Por favor, tente novamente ou entre em contato com o suporte.";

    public MedicoException() {
        super(DEFAULT_MESSAGE);
    }

    public MedicoException(String customMessage) {
        super(customMessage);
    }

}
