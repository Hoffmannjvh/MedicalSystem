package com.medicalsystem.medicalapi.repository;

import com.medicalsystem.medicalapi.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

    public interface MedicoRepository extends JpaRepository<Medico, UUID> {

    @Query("SELECT m FROM Medico m WHERE " +
            "(m.nome LIKE %:nome% OR :nome IS NULL) AND " +
            "(m.especialidade LIKE %:especialidade% OR :especialidade IS NULL) AND " +
            "(m.crm LIKE %:crm% OR :crm IS NULL)")
        List<Medico> findMedicosByFilters(@Param("nome") String nome,
                                          @Param("especialidade") String especialidade,
                                          @Param("crm") String crm);
}

