package com.medicalsystem.medicalapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.medicalsystem.medicalapi.utils.CustomCPFSerializer;
import com.medicalsystem.medicalapi.utils.CustomDateDeserializer;
import com.medicalsystem.medicalapi.utils.CustomPhoneSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private UUID id;

    @NotNull(message = "O campo 'nome' é obrigatório.")
    @Size(min = 2, max = 100, message = "O campo 'nome' deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotNull(message = "O campo 'cpf' é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "O 'cpf' deve conter apenas números e ter 11 dígitos.")
    @JsonSerialize(using = CustomCPFSerializer.class)
    private String cpf;

    @NotNull(message = "A 'dataNascimento' é obrigatória.")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @NotNull(message = "O campo 'contato' é obrigatório.")
    @Pattern(regexp = "^\\d{10,11}$", message = "O 'contato' deve conter apenas números e ter entre 10 e 11 dígitos com DDD.")
    @JsonSerialize(using = CustomPhoneSerializer.class)
    private String contato;

}

