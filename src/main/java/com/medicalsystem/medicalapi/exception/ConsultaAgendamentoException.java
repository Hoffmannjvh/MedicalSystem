package com.medicalsystem.medicalapi.exception;


public class ConsultaAgendamentoException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Houve um erro ao tentar agendar a consulta. Por favor, tente novamente ou entre em contato com o suporte.";

    public ConsultaAgendamentoException() {
        super(DEFAULT_MESSAGE);
    }

    public ConsultaAgendamentoException(String customMessage) {
        super(customMessage);
    }
}