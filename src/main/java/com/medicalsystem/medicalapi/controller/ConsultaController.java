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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
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
    @Operation(summary = "Listar todas as consultas", description = "Retorna uma lista de todas as consultas agendadas.")
    public ResponseEntity<Object> listarConsultas() {
        List<Consulta> consultas = consultaService.listarConsultas();
        //Validação se a listagem retorna vazio
        if (consultas.isEmpty()) {
            List<String> erros = new ArrayList<>();
            erros.add("Nenhuma consulta encontrada no sistema.");
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(404).body(errorsResponse);
        }
        return ResponseEntity.ok(consultas);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar informações de uma consulta", description = "Atualiza os dados da consulta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados da consulta atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dados da consulta não encontrado para o ID fornecido")
    })
    public ResponseEntity<Object> atualizarConsulta(
            @PathVariable @Parameter(description = "ID único do médico a ser atualizado") UUID id,
            @RequestBody ConsultaRequest consultaRequest) {
        try {
            Consulta consultaExistente = consultaService.buscarConsultaPorId(id);
            if (consultaExistente == null) {
                throw new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido");
            }
            Medico medico = medicoService.buscarMedicoPorId(consultaRequest.getMedico_id());
            Paciente paciente = pacienteService.buscarPacientePorId(consultaRequest.getPaciente_id());

            // Atualizar dados
            consultaExistente.setMedico_id(medico);
            consultaExistente.setPaciente_id(paciente);
            consultaExistente.setData_consulta(consultaRequest.getData_consulta());
            consultaExistente.setStatus_consulta(consultaRequest.getStatus());
            consultaService.atualizarConsulta(id, consultaExistente);
            return ResponseEntity.status(200).body(consultaExistente);

        } catch (ConsultaAgendamentoException | ConsultaNotFoundException e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro ao atualizar consulta: " + e.getMessage());
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(400).body(errorsResponse);

        } catch (Exception e) {
            List<String> erros = new ArrayList<>();
            erros.add("Erro interno ao atualizar consulta.");
            ErrorsResponse errorsResponse = new ErrorsResponse(erros);
            return ResponseEntity.status(500).body(errorsResponse);
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
