package com.medicalsystem.medicalapi.controller;

import com.medicalsystem.medicalapi.entity.Medico;
import com.medicalsystem.medicalapi.exception.MedicoNotFoundException;
import com.medicalsystem.medicalapi.model.ErroResponse;
import com.medicalsystem.medicalapi.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo médico", description = "Registra um novo médico no sistema com informações como nome e especialidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos: Nome ou Especialidade não informados."),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar o médico")
    })
    public ResponseEntity<Object> criarMedico(@RequestBody @Valid @Parameter(description = "Informações do médico a ser cadastrado") Medico medico, BindingResult result) {
        if (result.hasErrors()) {
            List<String> erros = new ArrayList<>();
            // Adiciona todos os erros encontrados na validação
            for (ObjectError error : result.getAllErrors()) {
                erros.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse(erros));
        }
        try {
            Medico novoMedico = medicoService.salvarMedico(medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoMedico);
        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro ao salvar o médico: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResponse(erros));
        }
    }

    // GET /medicos - Listar todos os médicos
    @GetMapping
    @Operation(summary = "Listar médicos com filtros", description = "Retorna uma lista de médicos com base em filtros opcionais como nome, especialidade ou CRM.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum médico encontrado com os critérios fornecidos")
    })
    public ResponseEntity<Object> listarMedicos(
            @RequestParam(required = false) @Parameter(description = "Filtrar médicos pelo nome (Digite o nome completo)") String nome,
            @RequestParam(required = false) @Parameter(description = "Filtrar médicos pela especialidade") String especialidade,
            @RequestParam(required = false) @Parameter(description = "Filtrar médicos pelo CRM") String crm) {

        List<Medico> medicos = medicoService.listarMedicos(nome, especialidade, crm);
        return ResponseEntity.ok(medicos);
    }

    // GET /medicos/{id} - Buscar médico por ID
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
            List<String> erros = new ArrayList<>();
            erros.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErroResponse(erros));

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao buscar médico.");
            return ResponseEntity.status(500).body(new ErroResponse(erros));
        }
    }

    // PUT /medicos/{id} - Atualizar informações de um médico
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de um médico", description = "Atualiza as informações de um médico, como nome, especialidade, CRM e e-mail.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
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
            return ResponseEntity.badRequest().body(new ErroResponse(erros));
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
            return ResponseEntity.status(404).body(new ErroResponse(erros));

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao atualizar médico.");
            return ResponseEntity.status(500).body(new ErroResponse(erros));
        }
    }

    // DELETE /medicos/{id} - Remover um médico
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um médico", description = "Deleta um médico do sistema com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado para o ID fornecido"),
            @ApiResponse(responseCode = "500", description = "Erro ao tentar remover o médico")
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
            List<String> erros = new ArrayList<>();
            erros.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErroResponse(erros));

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro ao tentar remover o médico: " + e.getMessage());
            return ResponseEntity.status(500).body(new ErroResponse(erros));
        }
    }
}
