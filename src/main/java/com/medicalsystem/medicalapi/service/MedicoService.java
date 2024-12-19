package com.medicalsystem.medicalapi.service;

import com.medicalsystem.medicalapi.entity.Medico;
import com.medicalsystem.medicalapi.exception.AgendarMedicoException;
import com.medicalsystem.medicalapi.exception.MedicoNotFoundException;
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
            throw new AgendarMedicoException("Erro ao salvar: " + e.getMessage());
        }
    }

    public List<Medico> listarMedicos() {
        List<Medico> medicos = medicoRepository.findAll();

        if (medicos.isEmpty()) {
            throw new MedicoNotFoundException("Nenhum médico encontrado no sistema.");
        }

        return medicos;
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

    public List<Medico> listarMedicosComFiltros(String nome, String especialidade, String crm) {
        // Validar se pelo menos um filtro foi passado
        if (nome == null && especialidade == null && crm == null) {
            throw new IllegalArgumentException("Pelo menos um filtro (nome, especialidade ou CRM) deve ser fornecido.");
        }

        return medicoRepository.findMedicosByFilters(nome, especialidade, crm);
    }
}
