package com.medicalsystem.medicalapi.controller;

import com.medicalsystem.medicalapi.entity.Medico;
import com.medicalsystem.medicalapi.exception.MedicoNotFoundException;
import com.medicalsystem.medicalapi.model.ErrorsResponse;
import com.medicalsystem.medicalapi.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;


    @PostMapping
    @Operation(summary = "Cadastrar um novo médico", description = "Permite cadastrar um médico no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico cadastrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Medico.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class)))
    })

    public ResponseEntity<Object> criarMedico(@RequestBody @Valid @Parameter(description = "Informações do médico a ser cadastrado") Medico medico, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(errors));
        }
        try {
            Medico novoMedico = medicoService.salvarMedico(medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoMedico);
        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro ao salvar o médico: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorsResponse(erros));
        }
    }

    @GetMapping
    @Operation(summary = "Listar médicos com filtros", description = "Retorna uma lista de médicos com base em filtros opcionais como nome, especialidade ou CRM.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum médico encontrado com os critérios fornecidos")
    })
    public ResponseEntity<Object> listarMedicos(
            @RequestParam(required = false) @Parameter(description = "Filtrar médicos pelo Nome (Digite o nome completo)") String nome,
            @RequestParam(required = false) @Parameter(description = "Filtrar médicos pela Especialidade") String especialidade,
            @RequestParam(required = false) @Parameter(description = "Filtrar médicos pelo CRM") String crm) {

        try {
            List<Medico> medicos = medicoService.listarMedicos(nome, especialidade, crm);
            return ResponseEntity.ok(medicos);

        } catch (Exception e) {
            String mensagemErro = "Nenhum dado encontrado com as informações fornecidas. Verifique e tente novamente";
            List<String> erros = Arrays.asList(mensagemErro);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erros);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar médico por ID", description = "Retorna os detalhes de um médico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado para o ID fornecido")
    })
    public ResponseEntity<Object> buscarMedicoPorId(
            @PathVariable @Parameter(description = "ID único do médico") UUID id) {
        try {
            Medico medico = medicoService.buscarMedicoPorId(id);
            return ResponseEntity.ok(medico);

        } catch (MedicoNotFoundException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(errors));

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao buscar médico.");
            return ResponseEntity.status(500).body(new ErrorsResponse(erros));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de um médico", description = "Permite atualizar todos os dados de um médico exstente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado para o ID fornecido")
    })
    public ResponseEntity<Object> atualizarMedico(
            @PathVariable @Parameter(description = "ID único do médico a ser atualizado") UUID id,
            @Valid @RequestBody @Parameter(description = "Informações atualizadas do médico") Medico medicoAtualizado,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> erros = new ArrayList<>();
            for (FieldError erro : result.getFieldErrors()) {
                erros.add(erro.getField() + ": " + erro.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ErrorsResponse(erros));
        }

        try {
            Medico medico = medicoService.atualizarMedico(id, medicoAtualizado);
            if (medico == null) {
                throw new MedicoNotFoundException("Médico não encontrado para o ID fornecido.");
            }
            return ResponseEntity.ok(medico);

        } catch (MedicoNotFoundException e) {
            List<String> erros = new ArrayList<>();
            erros.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(erros));

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao atualizar médico.");
            return ResponseEntity.status(500).body(new ErrorsResponse(erros));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um médico", description = "Deleta um médico do sistema com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado para o ID fornecido"),
    })
    public ResponseEntity<Object> deletarMedico(
            @PathVariable @Parameter(description = "ID único do médico a ser removido") UUID id) {
        try {
            boolean sucesso = medicoService.deletarMedico(id);
            if (!sucesso) {
                throw new MedicoNotFoundException("Médico não encontrado para o ID fornecido.");
            }
            return ResponseEntity.noContent().build();

        } catch (MedicoNotFoundException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(errors));

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Erro ao tentar remover o médico: " + e.getMessage());
            return ResponseEntity.status(500).body(new ErrorsResponse(errors));
        }
    }
}
