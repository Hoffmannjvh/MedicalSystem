package com.medicalsystem.medicalapi.entity;

import lombok.Getter;

@Getter
public enum StatusConsulta {

    AGENDADA("AGENDADA"),
    CANCELADA("CANCELADA"),
    CONCLUIDA("CONCLUIDA");

    private final String descricao;

    // Construtor para adicionar a descrição
    StatusConsulta(String descricao) {
        this.descricao = descricao;
    }


    // Sobrescrever o método toString para retornar a descrição diretamente
    @Override
    public String toString() {
        return descricao;
    }

    // Método para buscar o StatusConsulta pelo nome da descrição
    public static StatusConsulta fromDescricao(String descricao) {
        for (StatusConsulta status : StatusConsulta.values()) {
            if (status.getDescricao().equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de consulta inválido: " + descricao);
    }
}
