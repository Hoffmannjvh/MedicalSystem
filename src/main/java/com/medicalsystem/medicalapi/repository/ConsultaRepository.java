package com.medicalsystem.medicalapi.repository;

import com.medicalsystem.medicalapi.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {

    @Query("SELECT c FROM Consulta c WHERE c.paciente_id.id = :pacienteId")
    List<Consulta> findByPacienteId(@Param("pacienteId") UUID pacienteId);

    @Query("SELECT c FROM Consulta c WHERE c.medico_id.id = :medicoId")
    List<Consulta> findByMedicoId(@Param("medicoId") UUID medicoId);

    @Query("SELECT c FROM Consulta c WHERE c.paciente_id.id = :pacienteId AND c.medico_id.id = :medicoId")
    List<Consulta> findByPacienteIdAndMedicoId(@Param("pacienteId") UUID pacienteId, @Param("medicoId") UUID medicoId);

    @Query("SELECT c FROM Consulta c WHERE c.id = :consultaId")
    Optional<Consulta> findById(@Param("consultaId") UUID consultaId);



}




