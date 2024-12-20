package com.medicalsystem.medicalapi.entity;

import com.medicalsystem.medicalapi.constant.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Schema(description = "Informações sobre a consulta.")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "ID único da consulta", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    @NotNull(message = "Erro: O médico é obrigatório para agendar a consulta.")
    @Valid
    @Schema(description = "Médico responsável pela consulta.")
    private Medico medico_id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    @NotNull(message = "Erro: O paciente é obrigatório para agendar a consulta.")
    @Valid
    @Schema(description = "Paciente agendado para a consulta.")
    private Paciente paciente_id;

    private LocalDateTime data_consulta;

    @NotNull(message = "Erro: O status da consulta é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status da consulta", allowableValues = {"AGENDADA", "CANCELADA", "CONCLUIDA"})
    private StatusConsulta status_consulta;


}
