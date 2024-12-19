package com.medicalsystem.medicalapi.exception;

public class AgendarMedicoException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Houve um erro ao tentar salvar. Por favor, tente novamente ou entre em contato com o suporte.";

    public AgendarMedicoException() {
        super(DEFAULT_MESSAGE);
    }

    public AgendarMedicoException(String customMessage) {
        super(customMessage);
    }

}
