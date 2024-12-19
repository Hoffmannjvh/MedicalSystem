package com.medicalsystem.medicalapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private UUID id;

    @NotNull(message = "Campo obrigatório: nome.")
    @Size(min = 1, message = "Campo obrigatório: nome.")
    private String nome;

    @NotNull(message = "Campo obrigatório: Especialidade.")
    private String especialidade;

    @Pattern(regexp = "^[0-9]{4,6}$", message = "Campo obrigatório: O CRM deve conter de 4 a 6 dígitos numéricos.")
    @NotNull(message = "Campo obrigatório: CRM.")
    private String crm;

    @Email(message = "O e-mail deve ser válido.")
    @NotNull(message = "Campo obrigatório: E-MAIL.")
    private String email;
}
