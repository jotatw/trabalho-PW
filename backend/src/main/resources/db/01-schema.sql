-- ============================================================
-- TRABALHO PW
-- Schema inicial do banco de dados
-- PostgreSQL
-- ============================================================


-- ============================================================
-- PERFIS
-- Define os tipos de acesso disponíveis no sistema.
-- ============================================================

CREATE TABLE perfis (
                        id BIGSERIAL PRIMARY KEY,
                        nome VARCHAR(30) NOT NULL UNIQUE,
                        descricao VARCHAR(150)
);


-- ============================================================
-- USUARIOS
-- Armazena as contas utilizadas para acessar o sistema.
-- ============================================================

CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          senha_hash VARCHAR(255) NOT NULL,
                          criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE
);


-- ============================================================
-- USUARIO_PERFIS
-- Relaciona usuários aos seus perfis de acesso.
-- ============================================================

CREATE TABLE usuario_perfis (
                                usuario_id BIGINT NOT NULL,
                                perfil_id BIGINT NOT NULL,

                                PRIMARY KEY (usuario_id, perfil_id),

                                CONSTRAINT fk_usuario_perfis_usuario
                                    FOREIGN KEY (usuario_id)
                                        REFERENCES usuarios(id),

                                CONSTRAINT fk_usuario_perfis_perfil
                                    FOREIGN KEY (perfil_id)
                                        REFERENCES perfis(id)
);


-- ============================================================
-- CLIENTES
-- Armazena os clientes da locadora.
-- ============================================================

CREATE TABLE clientes (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(120) NOT NULL,
                          cpf VARCHAR(11) NOT NULL UNIQUE,
                          data_nascimento DATE NOT NULL,
                          telefone VARCHAR(20) NOT NULL,
                          email VARCHAR(150),
                          criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE
);


-- ============================================================
-- CATEGORIAS_VEICULOS
-- Define as categorias disponíveis para os veículos.
-- ============================================================

CREATE TABLE categorias_veiculos (
                                     id BIGSERIAL PRIMARY KEY,
                                     nome VARCHAR(50) NOT NULL UNIQUE,
                                     descricao VARCHAR(200),
                                     valor_diaria NUMERIC(10, 2) NOT NULL,

                                     CONSTRAINT ck_categorias_valor_diaria
                                         CHECK (valor_diaria >= 0),

                                     ativo BOOLEAN NOT NULL DEFAULT TRUE
);


-- ============================================================
-- VEICULOS
-- Armazena os veículos disponíveis na frota.
-- ============================================================

CREATE TABLE veiculos (
                          id BIGSERIAL PRIMARY KEY,
                          categoria_id BIGINT NOT NULL,
                          marca VARCHAR(50) NOT NULL,
                          modelo VARCHAR(80) NOT NULL,
                          ano SMALLINT NOT NULL,
                          placa VARCHAR(7) NOT NULL UNIQUE,
                          cor VARCHAR(30) NOT NULL,
                          quilometragem INTEGER NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE,

                          CONSTRAINT fk_veiculos_categoria
                              FOREIGN KEY (categoria_id)
                                  REFERENCES categorias_veiculos(id),

                          CONSTRAINT ck_veiculos_ano
                              CHECK (ano >= 1900),

                          CONSTRAINT ck_veiculos_quilometragem
                              CHECK (quilometragem >= 0),

                          CONSTRAINT ck_veiculos_status
                              CHECK (status IN (
                                                'DISPONIVEL',
                                                'ALUGADO',
                                                'MANUTENCAO',
                                                'INATIVO'
                                  ))
);


-- ============================================================
-- RESERVAS
-- Registra reservas realizadas pelos clientes.
-- ============================================================

CREATE TABLE reservas (
                          id BIGSERIAL PRIMARY KEY,
                          cliente_id BIGINT NOT NULL,
                          categoria_id BIGINT NOT NULL,
                          data_inicio TIMESTAMPTZ NOT NULL,
                          data_fim TIMESTAMPTZ NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          observacoes VARCHAR(500),

                          CONSTRAINT fk_reservas_cliente
                              FOREIGN KEY (cliente_id)
                                  REFERENCES clientes(id),

                          CONSTRAINT fk_reservas_categoria
                              FOREIGN KEY (categoria_id)
                                  REFERENCES categorias_veiculos(id),

                          CONSTRAINT ck_reservas_datas
                              CHECK (data_fim > data_inicio),

                          CONSTRAINT ck_reservas_status
                              CHECK (status IN (
                                                'PENDENTE',
                                                'CONFIRMADA',
                                                'CANCELADA',
                                                'CONCLUIDA'
                                  ))
);


-- ============================================================
-- LOCACOES
-- Registra as locações efetivamente realizadas.
-- ============================================================

CREATE TABLE locacoes (
                          id BIGSERIAL PRIMARY KEY,
                          reserva_id BIGINT,
                          cliente_id BIGINT NOT NULL,
                          veiculo_id BIGINT NOT NULL,
                          inicio TIMESTAMPTZ NOT NULL,
                          previsao_devolucao TIMESTAMPTZ NOT NULL,
                          devolucao TIMESTAMPTZ,
                          valor_diaria NUMERIC(10, 2) NOT NULL,
                          valor_total NUMERIC(10, 2),
                          status VARCHAR(20) NOT NULL,
                          criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT uq_locacoes_reserva
                              UNIQUE (reserva_id),

                          CONSTRAINT fk_locacoes_reserva
                              FOREIGN KEY (reserva_id)
                                  REFERENCES reservas(id),

                          CONSTRAINT fk_locacoes_cliente
                              FOREIGN KEY (cliente_id)
                                  REFERENCES clientes(id),

                          CONSTRAINT fk_locacoes_veiculo
                              FOREIGN KEY (veiculo_id)
                                  REFERENCES veiculos(id),

                          CONSTRAINT ck_locacoes_previsao
                              CHECK (previsao_devolucao > inicio),

                          CONSTRAINT ck_locacoes_devolucao
                              CHECK (devolucao IS NULL OR devolucao >= inicio),

                          CONSTRAINT ck_locacoes_valor_diaria
                              CHECK (valor_diaria >= 0),

                          CONSTRAINT ck_locacoes_valor_total
                              CHECK (valor_total IS NULL OR valor_total >= 0),

                          CONSTRAINT ck_locacoes_status
                              CHECK (status IN (
                                                'ATIVA',
                                                'CONCLUIDA',
                                                'CANCELADA'
                                  ))
);


-- ============================================================
-- SESSOES
-- Registra os períodos de utilização do sistema pelos usuários.
-- ============================================================

CREATE TABLE sessoes (
                         id BIGSERIAL PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         inicio TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         fim TIMESTAMPTZ,
                         ip_origem VARCHAR(45),
                         user_agent VARCHAR(500),
                         status VARCHAR(20) NOT NULL,

                         CONSTRAINT fk_sessoes_usuario
                             FOREIGN KEY (usuario_id)
                                 REFERENCES usuarios(id),

                         CONSTRAINT ck_sessoes_fim
                             CHECK (fim IS NULL OR fim >= inicio),

                         CONSTRAINT ck_sessoes_status
                             CHECK (status IN (
                                               'ATIVA',
                                               'ENCERRADA',
                                               'EXPIRADA'
                                 ))
);


-- ============================================================
-- LOGS
-- Registra eventos de uso, erros e falhas da aplicação.
-- ============================================================

CREATE TABLE logs (
                      id BIGSERIAL PRIMARY KEY,
                      usuario_id BIGINT,
                      sessao_id BIGINT,
                      tipo VARCHAR(20) NOT NULL,
                      nivel VARCHAR(20) NOT NULL,
                      acao VARCHAR(100) NOT NULL,
                      mensagem TEXT NOT NULL,
                      data_hora TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                      CONSTRAINT fk_logs_usuario
                          FOREIGN KEY (usuario_id)
                              REFERENCES usuarios(id),

                      CONSTRAINT fk_logs_sessao
                          FOREIGN KEY (sessao_id)
                              REFERENCES sessoes(id),

                      CONSTRAINT ck_logs_tipo
                          CHECK (tipo IN (
                                          'USO',
                                          'ERRO',
                                          'CRASH'
                              )),

                      CONSTRAINT ck_logs_nivel
                          CHECK (nivel IN (
                                           'INFO',
                                           'WARN',
                                           'ERROR',
                                           'FATAL'
                              ))
);


-- ============================================================
-- INDICES
-- Índices para consultas frequentes de auditoria.
-- ============================================================

CREATE INDEX idx_logs_usuario
    ON logs (usuario_id);

CREATE INDEX idx_logs_sessao
    ON logs (sessao_id);

CREATE INDEX idx_logs_data_hora
    ON logs (data_hora);