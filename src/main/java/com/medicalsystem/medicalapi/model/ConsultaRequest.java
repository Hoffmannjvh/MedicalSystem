package com.medicalsystem.medicalapi.model;

import com.medicalsystem.medicalapi.entity.StatusConsulta;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConsultaRequest {

    private UUID medico_id;
    private UUID paciente_id;
    private LocalDateTime data_consulta;
    private StatusConsulta status;
}
