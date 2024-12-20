package com.medicalsystem.medicalapi.service;

import com.medicalsystem.medicalapi.entity.Medico;
import com.medicalsystem.medicalapi.exception.MedicoException;
import com.medicalsystem.medicalapi.exception.MedicoNotFoundException;
import com.medicalsystem.medicalapi.exception.PacienteNotFound;
import com.medicalsystem.medicalapi.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public Medico salvarMedico(Medico medico) {
        try {
            return medicoRepository.save(medico);

        } catch (Exception e) {
            throw new MedicoException("Erro ao salvar: " + e.getMessage());
        }
    }

    public List<Medico> listarMedicos(String nome, String especialidade, String crm) {
        List<Medico> listarMedicos;

        if (nome == null && especialidade == null && crm == null) {
            return medicoRepository.findAll();
        }

        listarMedicos = medicoRepository.findMedicosByFilters(nome, especialidade, crm);

        if (listarMedicos.isEmpty()) {
            throw new MedicoNotFoundException("Não foram encontrados registros com os dados Nome: " + nome + " Especialidade: " + especialidade + " CRM: " + crm);
        }

        return listarMedicos;

    }

    public Medico buscarMedicoPorId(UUID id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNotFoundException("Medico não encontrado com ID: " + id));
    }

    // Atualizar dados do médico
    public Medico atualizarMedico(UUID id, Medico medico) {
        if (!medicoRepository.existsById(id)) {
            throw new MedicoNotFoundException("Médico não encontrado com ID: " + id);
        }

        medico.setId(id);
        return medicoRepository.save(medico);
    }

    // Deletar médico
    public boolean deletarMedico(UUID id) {
        if (!medicoRepository.existsById(id)) {
            throw new MedicoNotFoundException("Médico não encontrado com ID: " + id);
        }

        medicoRepository.deleteById(id);
        return true;
    }
}
