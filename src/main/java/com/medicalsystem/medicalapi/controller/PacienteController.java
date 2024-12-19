package com.medicalsystem.medicalapi.controller;

import com.medicalsystem.medicalapi.entity.Paciente;
import com.medicalsystem.medicalapi.exception.InvalidDateFormatException;
import com.medicalsystem.medicalapi.exception.PacienteNotFound;
import com.medicalsystem.medicalapi.model.ErroResponse;
import com.medicalsystem.medicalapi.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Operation(summary = "Cadastrar um novo paciente", description = "Permite cadastrar um paciente no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Paciente.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponse.class)))
    })

    @PostMapping
    public ResponseEntity<Object> salvarPaciente(@Valid @RequestBody Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return new ResponseEntity<>(new ErroResponse(errors), HttpStatus.BAD_REQUEST);
        }

        Paciente savedPaciente = pacienteService.salvarPaciente(paciente);
        return new ResponseEntity<>(savedPaciente, HttpStatus.CREATED);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<String> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Listar pacientes com filtros", description = "Retorna uma lista de pacientes com filtros opcionais (Nome ou CPF).")
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf) {

        // Chamada para o resultado dos filtros
        List<Paciente> pacientes = pacienteService.listarPacientes(nome, cpf);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    @ExceptionHandler(PacienteNotFound.class)
    public ResponseEntity<String> handlePacienteNotFoundException(PacienteNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Buscar paciente por ID", description = "Retorna os detalhes de um paciente pelo seu ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPacientePorId(@PathVariable UUID id) {
        Optional<Paciente> paciente = Optional.ofNullable(pacienteService.buscarPacientePorId(id));
        if (paciente.isPresent()) {
            return new ResponseEntity<>(paciente.get(), HttpStatus.OK);
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Paciente não encontrado para o ID fornecido.");
            return new ResponseEntity<>(new ErroResponse(errors), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Atualizar um paciente", description = "Permite atualizar os dados de um paciente existente.")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable UUID id, @Valid @RequestBody Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return new ResponseEntity<>(new ErroResponse(errors), HttpStatus.BAD_REQUEST);
        }

        Paciente updatedPaciente = pacienteService.atualizarPaciente(id, paciente);
        if (updatedPaciente != null) {
            return new ResponseEntity<>(updatedPaciente, HttpStatus.OK);
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Paciente não encontrado para o ID fornecido.");
            return new ResponseEntity<>(new ErroResponse(errors), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deletar paciente", description = "Remove um paciente pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarPaciente(@PathVariable UUID id) {
        boolean isDeleted = pacienteService.deletarPaciente(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Paciente não encontrado para o ID fornecido.");
            return new ResponseEntity<>(new ErroResponse(errors), HttpStatus.NOT_FOUND);
        }
    }
}
