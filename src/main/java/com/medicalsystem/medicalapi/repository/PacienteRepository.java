package com.medicalsystem.medicalapi.repository;

import com.medicalsystem.medicalapi.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    @Query("SELECT p FROM Paciente p WHERE  p.nome LIKE %:nome%")
    List<Paciente> findPacientesByName(@Param("nome") String nome);

    @Query("SELECT p FROM Paciente p WHERE  p.cpf = :cpf")
    List<Paciente> findPacientesByCPF(@Param("cpf") String cpf);

    @Query("SELECT p FROM Paciente p WHERE p.nome = :nome AND p.cpf = :cpf")
    List<Paciente> findPacientesByNameAndCPF(@Param("nome") String nome, @Param("cpf") String cpf);


}
