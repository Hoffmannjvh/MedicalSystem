package com.medicalsystem.medicalapi.controller;

import com.medicalsystem.medicalapi.entity.Paciente;
import com.medicalsystem.medicalapi.exception.InvalidDateFormatException;
import com.medicalsystem.medicalapi.exception.PacienteNotFound;
import com.medicalsystem.medicalapi.model.ErrorsResponse;
import com.medicalsystem.medicalapi.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Operações relacionadas a pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Operation(summary = "Cadastrar um novo paciente", description = "Permite cadastrar um paciente no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Paciente.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class)))
    })

    @PostMapping
    public ResponseEntity<Object> salvarPaciente(@Valid @RequestBody Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(errors));
        }
        try {
            Paciente novoPaciente = pacienteService.salvarPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro ao salvar o paciente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorsResponse(erros));
        }
    }

    @GetMapping
    @Operation(summary = "Listar pacientes com filtros", description = "Retorna uma lista de pacientes com filtros opcionais (Nome ou CPF).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum paciente encontrado com os critérios fornecidos")
    })
    public ResponseEntity<List<Paciente>> listarPacientes(
            @RequestParam(required = false) @Parameter(description = "Filtrar pacientes pelo Nome (Digite o nome completo)") String nome,
            @RequestParam(required = false) @Parameter(description = "Filtrar pacientes pelo CPF (Digite o CPF completo sem pontuações)") String cpf) {

        List<Paciente> pacientes = pacienteService.listarPacientes(nome, cpf);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Retorna os detalhes de um paciente com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado para o ID fornecido")
    })
    public ResponseEntity<Object> buscarPacientePorId(
            @PathVariable @Parameter(description = "ID único do paciente") UUID id) {
        try {
            Paciente paciente = pacienteService.buscarPacientePorId(id);
            return ResponseEntity.ok(paciente);

        } catch (PacienteNotFound e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(errors));

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Erro interno ao buscar paciente, verifique as informações e tente novamente.");
            return ResponseEntity.status(500).body(new ErrorsResponse(errors));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de um paciente", description = "Permite atualizar todos os dados de um paciente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado para o ID fornecido")
    })
    public ResponseEntity<Object> atualizarPaciente(
            @PathVariable @Parameter(description = "ID único do paciente a ser atualizado") UUID id,
            @Valid @RequestBody @Parameter(description = "Informações atualizadas do paciente") Paciente pacienteAtualizado,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> erros = new ArrayList<>();
            for (FieldError erro : result.getFieldErrors()) {
                erros.add(erro.getField() + ": " + erro.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ErrorsResponse(erros));
        }

        try {
            Paciente paciente = pacienteService.atualizarPaciente(id, pacienteAtualizado);
            if (paciente == null) {
                throw new PacienteNotFound("Paciente não encontrado para o ID fornecido.");
            }
            return ResponseEntity.ok(paciente);

        } catch (PacienteNotFound e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(errors));

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Erro interno ao atualizar paciente.");
            return ResponseEntity.status(500).body(new ErrorsResponse(errors));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um paciente", description = "Deleta um paciente do sistema com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado para o ID fornecido"),
    })
    public ResponseEntity<Object> deletarPaciente(
            @PathVariable @Parameter(description = "ID único do paciente a ser removido") UUID id) {
        try {
            boolean sucesso = pacienteService.deletarPaciente(id);
            if (!sucesso) {
                throw new PacienteNotFound("Paciente não encontrado para o ID fornecido.");
            }
            return ResponseEntity.noContent().build();

        } catch (PacienteNotFound e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(errors));

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Erro ao tentar remover o paciente: " + e.getMessage());
            return ResponseEntity.status(500).body(new ErrorsResponse(errors));
        }

    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<String> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PacienteNotFound.class)
    public ResponseEntity<String> handlePacienteNotFoundException(PacienteNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
