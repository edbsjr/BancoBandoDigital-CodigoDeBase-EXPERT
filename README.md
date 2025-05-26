# 📌 Banco Digital

Este projeto é uma API REST de um banco digital, desenvolvida em Java com o framework Spring Boot.
O objetivo é aplicar conceitos de orientação a objetos, boas práticas de API e persistência de dados.
---

## 🚀 Funcionalidades

- ✅ Cadastro e gerenciamento de clientes
- ✅ Criação de contas - corrente e poupança
- ✅ Emissão de cartões - crédito e débito
- ✅ Realização de pagamentos e simulação de saldo
- ✅ Tratamento de exceções robusto com handlers globais
- ✅ Arquitetura baseada em camadas (Controller, Service, DAO, etc.)
- ✅ Sistema de logs para monitoramento de operações
---

## 🛠️ Tecnologias

- Java  
- Spring Boot
- Spring JDBC Template 
- Postman
- Lombok
- PostgreSQL
---

## 📂 Estrutura do Projeto

src/main/java/br/com/cdb/BandoDigitalFinal2/exceptions — Classes de exceções personalizadas da aplicação
src/main/java/br/com/cdb/BandoDigitalFinal2/handler — Global Exception Handler para tratamento centralizado de erros
src/main/java/br/com/cdb/BandoDigitalFinal2/util — Classes utilitárias diversas
src/main/resources/sql — Scripts SQL para criação de tabelas, funções e procedures no banco de dados
- `src/controller` — Endpoints da API
- `src/dao` — Classes de acesso a dados (DAO), responsáveis pela interação direta com o PostgreSQL via JDBC Template  
- `src/dto` — Objetos de transferência de dados (DTOs)  
- `src/entity` — Modelos de domínio (entidades)
- `src/repository` — Interfaces para acesso ao banco com Spring Data JPA  
- `src/service` — Regras de negócio e lógica da aplicação  
- `src/enums` — Enums utilizados no projeto
- `src/exceptions` — Classes de exceções personalizadas da aplicação
- `src/util` — Classes utilitárias diversas
- `src/sql` — Scripts SQL para criação de tabelas, funções e procedures no banco de dados 
  

Outros arquivos importantes:
- `README.md` — Documentação do projeto  
- `pom.xml` — Arquivo de build com as dependências do Maven
- `application.properties` — Configurações da aplicação (conexão com BD, portas, etc.)


## 📡 Endpoints da API Banco Digital

### 🧍 Cliente

- `POST /clientes` — Criar um novo cliente  
- `GET /clientes/{id}` — Obter detalhes de um cliente  
- `PUT /clientes/{id}` — Atualizar informações de um cliente  
- `DELETE /clientes/{id}` — Remover um cliente  
- `GET /clientes` — Listar todos os clientes

### 💳 Conta

- `POST /contas` — Criar uma nova conta  
- `GET /contas/{id}` — Obter detalhes de uma conta  
- `POST /contas/{id}/transferencia` — Realizar uma transferência entre contas  
- `GET /contas/{id}/saldo` — Consultar saldo da conta  
- `POST /contas/{id}/pix` — Realizar um pagamento via Pix  
- `POST /contas/{id}/deposito` — Realizar um depósito na conta  
- `POST /contas/{id}/saque` — Realizar um saque da conta  
- `PUT /contas/{id}/manutencao` — Aplicar taxa de manutenção mensal (para conta corrente)  
- `PUT /contas/{id}/rendimentos` — Aplicar rendimentos (para conta poupança)

### 💳 Cartão

- `POST /cartoes` — Emitir um novo cartão  
- `GET /cartoes/{id}` — Obter detalhes de um cartão  
- `POST /cartoes/{id}/pagamento` — Realizar um pagamento com o cartão  
- `PUT /cartoes/{id}/limite` — Alterar limite do cartão  
- `PUT /cartoes/{id}/status` — Ativar ou desativar um cartão  
- `PUT /cartoes/{id}/senha` — Alterar senha do cartão  
- `GET /cartoes/{id}/fatura` — Consultar fatura do cartão de crédito  
- `POST /cartoes/{id}/fatura/pagamento` — Realizar pagamento da fatura do cartão de crédito  
- `PUT /cartoes/{id}/limite-diario` — Alterar limite diário do cartão de débito
