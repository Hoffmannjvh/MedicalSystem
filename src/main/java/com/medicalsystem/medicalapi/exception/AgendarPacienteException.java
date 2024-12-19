package com.medicalsystem.medicalapi.exception;

public class AgendarPacienteException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Houve um erro ao tentar salvar. Por favor, tente novamente ou entre em contato com o suporte.";

    public AgendarPacienteException() {
        super(DEFAULT_MESSAGE);
    }

    public AgendarPacienteException(String customMessage) {
        super(customMessage);
    }

}
