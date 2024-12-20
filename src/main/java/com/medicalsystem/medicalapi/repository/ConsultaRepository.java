package com.medicalsystem.medicalapi.repository;

import com.medicalsystem.medicalapi.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

//Interface para o JpaRepository acessar a manipular o DB com menos códigos
public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {


}
