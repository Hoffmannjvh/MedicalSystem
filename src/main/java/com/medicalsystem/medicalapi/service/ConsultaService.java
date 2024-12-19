package com.medicalsystem.medicalapi.service;

import com.medicalsystem.medicalapi.entity.Consulta;
import com.medicalsystem.medicalapi.entity.StatusConsulta;
import com.medicalsystem.medicalapi.repository.ConsultaRepository;
import com.medicalsystem.medicalapi.exception.ConsultaAgendamentoException;
import com.medicalsystem.medicalapi.exception.ConsultaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Consulta> listarConsultas() {
        try {
            return consultaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar consultas: " + e.getMessage());
        }
    }

    public Consulta buscarConsultaPorId(UUID id) {
        try {
            return consultaRepository.findById(id)
                    .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido."));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar consulta por ID: " + e.getMessage());
        }
    }

    @Transactional
    public void cancelarConsulta(UUID id) {
        try {
            Consulta consulta = consultaRepository.findById(id)
                    .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido."));
            // Alterando o status para "Cancelada"
            consulta.setStatus(StatusConsulta.CANCELADA);
            consultaRepository.save(consulta);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar cancelar consulta: " + e.getMessage());
        }
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
    public void deletarConsulta(UUID id) {
        try {
            consultaRepository.findById(id)
                    .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada para o ID fornecido."));
            consultaRepository.deleteById(id);

        } catch (IllegalArgumentException e) {
            throw new ConsultaNotFoundException("Erro ao deletar consulta: " + e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar deletar consulta: " + e.getMessage());
        }
    }
}
