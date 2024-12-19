package com.medicalsystem.medicalapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.medicalsystem.medicalapi.model.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Erro para dados inválidos ou ausentes na consulta de agendamento
    @ExceptionHandler(ConsultaAgendamentoException.class)
    public ResponseEntity<ErroResponse> handleConsultaAgendamentoException(ConsultaAgendamentoException ex) {
        List<String> erros = new ArrayList<>();
        erros.add("Erro ao tentar agendar a consulta. Verifique se todos os campos obrigatórios foram preenchidos corretamente e tente novamente.");
        ErroResponse erro = new ErroResponse(erros);
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    // Erro para médico não encontrado
    @ExceptionHandler(MedicoNotFoundException.class)
    public ResponseEntity<ErroResponse> handleMedicoNotFoundException(MedicoNotFoundException ex) {
        List<String> erros = new ArrayList<>();
        erros.add("Médico não encontrado. Verifique o ID fornecido e tente novamente.");
        ErroResponse erro = new ErroResponse(erros);
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    // Erro para paciente não encontrado
    @ExceptionHandler(PacienteNotFound.class)
    public ResponseEntity<ErroResponse> handleNoPatientsFoundException(PacienteNotFound ex) {
        List<String> erros = new ArrayList<>();
        erros.add(ex.getMessage());  // Aqui a mensagem que você configurou na exceção será usada
        ErroResponse erro = new ErroResponse(erros);
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND); // Retorna o status 404 (não encontrado)
    }

    // Erro para consulta não encontrada
    @ExceptionHandler(ConsultaNotFoundException.class)
    public ResponseEntity<ErroResponse> handleConsultaNotFoundException(ConsultaNotFoundException ex) {
        List<String> erros = new ArrayList<>();
        erros.add("Consulta não encontrada. Verifique o ID fornecido e tente novamente.");
        ErroResponse erro = new ErroResponse(erros);
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    // Erro ao tentar agendar um médico
    @ExceptionHandler(AgendarMedicoException.class)
    public ResponseEntity<ErroResponse> handleAgendarMedicoException(AgendarMedicoException ex) {
        List<String> erros = new ArrayList<>();
        erros.add("Houve um erro ao tentar salvar as informações do médico. Verifique os dados fornecidos e tente novamente.");
        ErroResponse erro = new ErroResponse(erros);
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Erro ao tentar agendar um paciente
    @ExceptionHandler(AgendarPacienteException.class)
    public ResponseEntity<ErroResponse> handleAgendarPacienteException(AgendarPacienteException ex) {
        List<String> erros = new ArrayList<>();
        erros.add("Houve um erro ao tentar salvar as informações do paciente. Verifique os dados fornecidos e tente novamente.");
        ErroResponse erro = new ErroResponse(erros);
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "O formato fornecido para um campo é inválido.");
        errors.put("message", "Certifique-se de preencher a data de nascimento no formato 'dd/MM/yyyy'.");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


}
