package com.medicalsystem.medicalapi.controller;

import com.medicalsystem.medicalapi.entity.Consulta;
import com.medicalsystem.medicalapi.entity.Medico;
import com.medicalsystem.medicalapi.entity.Paciente;
import com.medicalsystem.medicalapi.exception.ConsultaAgendamentoException;
import com.medicalsystem.medicalapi.exception.ConsultaNotFoundException;
import com.medicalsystem.medicalapi.model.ConsultaRequest;
import com.medicalsystem.medicalapi.model.ErrorsResponse;
import com.medicalsystem.medicalapi.service.ConsultaService;
import com.medicalsystem.medicalapi.service.MedicoService;
import com.medicalsystem.medicalapi.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Operações relacionadas a consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;

    @PostMapping
    @Operation(summary = "Agendar uma nova consulta", description = "Agende uma nova consulta entre o paciente e o médico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, verifique e tente novamente.")
    })
    public ResponseEntity<Object> agendarConsulta(@RequestBody ConsultaRequest consultaRequest) {
        try {
            Medico medico = medicoService.buscarMedicoPorId(consultaRequest.getMedico_id());
            Paciente paciente = pacienteService.buscarPacientePorId(consultaRequest.getPaciente_id());

            // Criar consulta com objetos completos
            Consulta novaConsulta = new Consulta();
            novaConsulta.setMedico_id(medico);
            novaConsulta.setPaciente_id(paciente);
            novaConsulta.setData_consulta(consultaRequest.getData_consulta());
            novaConsulta.setStatus_consulta(consultaRequest.getStatus());
            consultaService.agendarConsulta(novaConsulta);
            return ResponseEntity.status(200).body(novaConsulta);

        } catch (ConsultaAgendamentoException e) {
            List<String> errors = new ArrayList<>();
            errors.add("Erro ao agendar consulta: " + e.getMessage());
            ErrorsResponse errorsResponse = new ErrorsResponse(errors);
            return ResponseEntity.status(400).body(errorsResponse);

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao agendar consulta, verifique os dados e tente novamente.");
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(500).body(errorsResponse);
        }
    }

    @GetMapping
    @Operation(summary = "Lista todas as consultas com filtros", description = "Retorna uma lista de todas as consultas com ou sem filtros.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de consultas retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma consulta encontrada com os critérios fornecidos")
    })
    public ResponseEntity<Object> listarConsultas(
            @RequestParam(required = false) @Parameter(description = "Filtrar as consultas pelo ID da consulta") UUID consultaId,
            @RequestParam(required = false) @Parameter(description = "Filtrar as consultas pelo ID do paciente") UUID pacienteId,
            @RequestParam(required = false) @Parameter(description = "Filtrar as consultas pelo ID do médico") UUID medicoId) {

        try {
            // Chama o serviço para listar consultas com base nos filtros fornecidos
            List<Consulta> consultas = consultaService.listarConsultas(consultaId, pacienteId, medicoId);

            if (consultas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonList("Nenhuma consulta encontrada com os filtros fornecidos."));
            }

            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("Erro ao tentar listar as consultas."));
        }
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de uma consulta", description = "Atualiza os dados de uma consulta existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta, médico ou paciente não encontrado para os IDs fornecidos"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou entrada inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar consulta")
    })
    public ResponseEntity<Object> atualizarConsulta(
            @PathVariable @Parameter(description = "ID único da consulta a ser atualizada") UUID id,
            @Valid @RequestBody @Parameter(description = "Dados atualizados da consulta") ConsultaRequest consultaRequest,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> erros = new ArrayList<>();
            for (FieldError erro : result.getFieldErrors()) {
                erros.add(erro.getField() + ": " + erro.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ErrorsResponse(erros));
        }

        try {
            // Verificar se a consulta existe
            Consulta consultaExistente = consultaService.buscarConsultaPorId(id);
            if (consultaExistente == null) {
                throw new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido.");
            }

            // Verificar se o médico existe
            Medico medico = medicoService.buscarMedicoPorId(consultaRequest.getMedico_id());
            if (medico == null) {
                throw new ConsultaNotFoundException("Médico não encontrado para o ID fornecido.");
            }

            // Verificar se o paciente existe
            Paciente paciente = pacienteService.buscarPacientePorId(consultaRequest.getPaciente_id());
            if (paciente == null) {
                throw new ConsultaNotFoundException("Paciente não encontrado para o ID fornecido.");
            }

            // Atualizar dados da consulta
            consultaExistente.setMedico_id(medico);
            consultaExistente.setPaciente_id(paciente);
            consultaExistente.setData_consulta(consultaRequest.getData_consulta());
            consultaExistente.setStatus_consulta(consultaRequest.getStatus());
            consultaService.atualizarConsulta(id, consultaExistente);

            return ResponseEntity.status(200).body(consultaExistente);

        } catch (ConsultaNotFoundException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(404).body(new ErrorsResponse(errors));

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Erro interno ao atualizar consulta. Verifique as informações e tente novamente");
            return ResponseEntity.status(500).body(new ErrorsResponse(errors));
        }
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna a consulta agendada pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<Object> buscarConsultaPorId(@PathVariable UUID id) {
        try {
            Consulta consulta = consultaService.buscarConsultaPorId(id);
            if (consulta == null) {
                throw new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido");
            }
            return ResponseEntity.ok(consulta);

        } catch (ConsultaNotFoundException e) {
            List<String> erros = new ArrayList<>();
            erros.add(e.getMessage());
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(404).body(errorsResponse);

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao buscar consulta, verifique os dados e tente novamente.");
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(500).body(errorsResponse);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar uma consulta", description = "Cancela uma consulta agendada pelo ID.")
    @ApiResponse(responseCode = "204", description = "Consulta cancelada com sucesso")
    public ResponseEntity<Object> cancelarConsulta(@PathVariable UUID id) {
        try {
            consultaService.cancelarConsulta(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add(e.getMessage());
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(400).body(errorsResponse);
        }
    }
}
