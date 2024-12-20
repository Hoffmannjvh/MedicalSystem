package com.medicalsystem.medicalapi.service;

import com.medicalsystem.medicalapi.entity.Paciente;
import com.medicalsystem.medicalapi.exception.PacienteException;
import com.medicalsystem.medicalapi.exception.PacienteNotFound;
import com.medicalsystem.medicalapi.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;


    public Paciente salvarPaciente(Paciente paciente) {
        try {
            return pacienteRepository.save(paciente);

        } catch (Exception e) {
            throw new PacienteException("Erro ao salvar o paciente: " + e.getMessage());
        }
    }

    public List<Paciente> listarPacientes(String nome, String cpf) {
        List<Paciente> pacientes;

        if (nome == null && cpf == null) {
            return pacienteRepository.findAll();
        }

        // Verifica se o CPF contém pontuação e remove se necessário
        if (cpf != null && cpf.matches(".*[\\.\\-\\/]")) {
            cpf = cpf.replaceAll("[\\.\\-\\/]", "");
        }

        if (nome != null && cpf != null) {
            pacientes = pacienteRepository.findPacientesByNameAndCPF(nome, cpf);
        } else if (nome != null) {
            pacientes = pacienteRepository.findPacientesByName(nome);
        } else {
            pacientes = pacienteRepository.findPacientesByCPF(cpf);
        }

        // Verifica se a lista de pacientes está vazia
        if (pacientes.isEmpty()) {
            if (nome != null && cpf != null) {
                throw new PacienteNotFound("Não foram encontrados registros para o paciente com nome: " + nome + " e CPF: " + cpf);
            } else if (cpf != null) {
                throw new PacienteNotFound("Não foram encontrados registros para o CPF: " + cpf + ". Lembre-se de pesquisar sem pontuação.");
            } else {
                throw new PacienteNotFound("Não foram encontrados registros para o nome: " + nome);
            }
        }

        return pacientes;
    }


    public Paciente buscarPacientePorId(UUID id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNotFound("Paciente não encontrado com ID: " + id));
    }

    // Atualiza os dados do paciente
    public Paciente atualizarPaciente(UUID id, Paciente paciente) {
        if (!pacienteRepository.existsById(id)) {
            throw new PacienteNotFound("Paciente não encontrado com ID: " + id);
        }

        paciente.setId(id);
        return pacienteRepository.save(paciente);
    }

    public boolean deletarPaciente(UUID id) {
        if (!pacienteRepository.existsById(id)) {
            throw new PacienteNotFound("Paciente não encontrado com ID: " + id);
        }

        pacienteRepository.deleteById(id);
        return true;
    }
}
