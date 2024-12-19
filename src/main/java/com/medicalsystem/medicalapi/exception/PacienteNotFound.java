package com.medicalsystem.medicalapi.exception;

public class PacienteNotFound extends RuntimeException {

    private static final String DEFAULT_MESSAGE_CPF = "Não há pacientes cadastrados com o CPF informado. Digite apenas números e verifique novamente.";
    private static final String DEFAULT_MESSAGE_NOME = "Não há pacientes cadastrados com o nome informado. Verifique se o nome está completo e foi digitado corretamente.";

    public PacienteNotFound() {
        super(DEFAULT_MESSAGE_CPF);
    }

    public PacienteNotFound(String customMessage) {
        super(customMessage);
    }

    public PacienteNotFound(boolean isNome, String customMessage) {
        super(isNome ? DEFAULT_MESSAGE_NOME : DEFAULT_MESSAGE_CPF);
    }
}
