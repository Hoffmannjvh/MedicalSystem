package com.medicalsystem.medicalapi.service;

import com.medicalsystem.medicalapi.entity.Consulta;
import com.medicalsystem.medicalapi.constant.StatusConsulta;
import com.medicalsystem.medicalapi.exception.ConsultaAgendamentoException;
import com.medicalsystem.medicalapi.exception.ConsultaNotFoundException;
import com.medicalsystem.medicalapi.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional
    public Consulta agendarConsulta(Consulta consulta) {
        try {
            return consultaRepository.save(consulta);

        } catch (IllegalArgumentException e) {
            throw new ConsultaAgendamentoException("Erro ao validar consulta: " + e.getMessage());

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro de integridade de dados: " + e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar consulta: " + e.getMessage());
        }
    }

    public List<Consulta> listarConsultas(UUID consultaId, UUID pacienteId, UUID medicoId) {
        if (consultaId != null) {
            // Busca pela consultaId
            return consultaRepository.findById(consultaId)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        if (pacienteId != null && medicoId != null) {
            // Busca por pacienteId e medicoId
            return consultaRepository.findByPacienteIdAndMedicoId(pacienteId, medicoId);
        }

        if (pacienteId != null) {
            // Busca somente por pacienteId
            return consultaRepository.findByPacienteId(pacienteId);
        }

        if (medicoId != null) {
            // Busca somente por medicoId
            return consultaRepository.findByMedicoId(medicoId);
        }

        // Se nenhum filtro for fornecido, retorna todas as consultas
        return consultaRepository.findAll();
    }



    public Consulta buscarConsultaPorId(UUID id) {

        return consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido."));
    }

    @Transactional
    public Consulta atualizarConsulta(UUID id, Consulta consultaAtualizada) {
        try {
            return consultaRepository.save(consultaAtualizada);

        } catch (IllegalArgumentException e) {
            throw new ConsultaAgendamentoException("Erro ao atualizar consulta: " + e.getMessage());

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro de integridade de dados: " + e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar atualizar consulta: " + e.getMessage());
        }
    }

    @Transactional
    public void cancelarConsulta(UUID id) {
        try {
            Consulta consulta = consultaRepository.findById(id)
                    .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido."));
            // Alterando o status para "Cancelada"
            consulta.setStatus_consulta(StatusConsulta.CANCELADA);
            consultaRepository.save(consulta);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar cancelar consulta: " + e.getMessage());
        }
    }
}
