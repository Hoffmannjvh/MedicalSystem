CREATE TABLE medico (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100) NOT NULL,
    crm VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE paciente (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(20) UNIQUE NOT NULL
    contato VARCHAR(12) NOT NULL
);

CREATE TABLE consulta (
    id UUID PRIMARY KEY,
    medico_id UUID NOT NULL,
    paciente_id UUID NOT NULL,
    data_consulta TIMESTAMP NOT NULL,
    status_consulta varchar(16) check (status_consulta in ('AGENDADA','CANCELADA', 'CONCLUIDA')),
    FOREIGN KEY (medico_id) REFERENCES medico(id),
    FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);
