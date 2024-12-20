# Gerenciamento Médico API

Este é um sistema para gerenciamento de médicos, pacientes e consultas. A API foi desenvolvida utilizando Java 8, Spring Boot, PostgreSQL como banco de dados e DBeaver para gerenciar o banco de dados.

## Tecnologias Utilizadas

- **Java 8**: A linguagem de programação principal utilizada.
- **Spring Boot**: Framework para facilitar a criação de APIs RESTful.
- **PostgreSQL**: Banco de dados relacional utilizado para armazenar as informações do sistema.
- **DBeaver**: Ferramenta utilizada para gerenciar e visualizar o banco de dados PostgreSQL.
- **Maven**: Gerenciador de dependências e automação de builds.
- **Flyway**: Ferramenta para controle de migrações do banco de dados.


## Configuração do Banco de Dados

O projeto utiliza **PostgreSQL** como banco de dados relacional. Para configurar o banco de dados localmente, siga os passos abaixo:

1. Instale e configure o **PostgreSQL** no seu computador, se ainda não o fez.
2. Crie um banco de dados no PostgreSQL para este projeto. Você pode usar o DBeaver para facilitar a criação e visualização das tabelas. No DBeaver:
    - Conecte-se ao seu banco de dados PostgreSQL.
    - Crie um banco de dados chamado, por exemplo, `medico_db`.

3. No arquivo `src/main/resources/application.properties`, adicione as seguintes configurações:

```properties
spring.application.name=medical-api

# Configurações do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/medico_db
spring.datasource.username=admin
spring.datasource.password=Admin1234
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect

# Configurações de logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=trace

# Configuração do Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=1
```

4. No arquivo `src/main/resources/db_.migration/V1__Initial_schema.sql`, contém os SQL para rodar no banco para realizar testes caso necessário 