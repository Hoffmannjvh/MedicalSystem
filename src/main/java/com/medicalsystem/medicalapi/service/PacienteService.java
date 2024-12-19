package com.medicalsystem.medicalapi.service;

import com.medicalsystem.medicalapi.entity.Paciente;
import com.medicalsystem.medicalapi.exception.AgendarPacienteException;
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
            throw new AgendarPacienteException("Erro ao salvar o paciente: " + e.getMessage());
        }
    }

    public List<Paciente> listarPacientes(String nome, String cpf) {
        List<Paciente> pacientes;
        // Valida se os filtros estão preenchidos
        if (nome == null && cpf == null) {
            return pacienteRepository.findAll();
        }

        if (nome != null) {
            pacientes = pacienteRepository.findPacientesByName(nome);
        } else {
            pacientes = pacienteRepository.findPacientesByCPF(cpf);
        }

        if (pacientes.isEmpty()) {
            throw new PacienteNotFound("Não foram encontrados registros para as consultas nome: " + nome + " CPF:" + cpf);
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
