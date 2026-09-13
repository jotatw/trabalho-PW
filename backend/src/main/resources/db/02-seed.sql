-- ============================================================
-- PERFIS
-- Dados iniciais dos perfis de acesso.
-- ============================================================

INSERT INTO perfis (nome, descricao)
VALUES
    ('ADMIN', 'Acesso completo ao sistema'),
    ('ATENDENTE', 'Acesso às operações da locadora');

-- ============================================================
-- USUARIOS
-- Usuários iniciais para testes da aplicação.
-- As senhas armazenadas são hashes BCrypt.
-- ============================================================

INSERT INTO usuarios (nome, email, senha_hash)
VALUES
    (
        'Administrador',
        'admin@locadora.com',
        '$2a$10$ti6aKugUvbi2Q63WYpYa6emWQ0tXa.2q9bvwejVrlHk/3CROqE58S'
    ),
    (
        'Atendente',
        'atendente@locadora.com',
        '$2a$10$8EE3OYplcj8sf4sjsxGxj.8b9Vx8Xj12urnHUbJzePQNqMJmvGAk.'
    );
INSERT INTO usuario_perfis (usuario_id, perfil_id)
SELECT
    u.id,
    p.id
FROM usuarios u
         JOIN perfis p ON p.nome = 'ADMIN'
WHERE u.email = 'admin@locadora.com';

INSERT INTO usuario_perfis (usuario_id, perfil_id)
SELECT
    u.id,
    p.id
FROM usuarios u
         JOIN perfis p ON p.nome = 'ATENDENTE'
WHERE u.email = 'atendente@locadora.com';

-- ============================================================
-- CLIENTES
-- Dados de teste dos clientes da locadora.
-- ============================================================

INSERT INTO clientes (
    nome,
    cpf,
    data_nascimento,
    telefone,
    email
)
VALUES
    (
        'Carlos Eduardo Silva',
        '12345678901',
        '1995-04-12',
        '61999990001',
        'carlos.silva@email.com'
    ),
    (
        'Mariana Oliveira Santos',
        '23456789012',
        '1998-08-25',
        '61999990002',
        'mariana.santos@email.com'
    ),
    (
        'Rafael Almeida Costa',
        '34567890123',
        '1990-11-03',
        '61999990003',
        'rafael.costa@email.com'
    );

-- ============================================================
-- CATEGORIAS_VEICULOS
-- Dados de teste das categorias de veículos.
-- ============================================================

INSERT INTO categorias_veiculos (
    nome,
    descricao,
    valor_diaria
)
VALUES
    (
        'Economico',
        'Veículos compactos para uso urbano.',
        89.90
    ),
    (
        'Intermediario',
        'Veículos para uso urbano e viagens.',
        129.90
    ),
    (
        'SUV',
        'Veículos com maior espaço e capacidade.',
        189.90
    );

-- ============================================================
-- VEICULOS
-- Dados de teste da frota da locadora.
-- ============================================================

INSERT INTO veiculos (
    categoria_id,
    marca,
    modelo,
    ano,
    placa,
    cor,
    quilometragem,
    status
)
SELECT
    c.id,
    dados.marca,
    dados.modelo,
    dados.ano,
    dados.placa,
    dados.cor,
    dados.quilometragem,
    dados.status
FROM (
         VALUES
             ('Economico', 'Toyota', 'Yaris', 2023, 'ABC1D23', 'Prata', 32500, 'ALUGADO'),
             ('Economico', 'Chevrolet', 'Onix', 2024, 'DEF4G56', 'Branco', 18700, 'DISPONIVEL'),
             ('Intermediario', 'Toyota', 'Corolla', 2023, 'GHI7J89', 'Cinza', 41200, 'DISPONIVEL'),
             ('Intermediario', 'Honda', 'Civic', 2024, 'JKL0M12', 'Preto', 22300, 'ALUGADO'),
             ('SUV', 'Jeep', 'Compass', 2024, 'NOP3Q45', 'Branco', 15600, 'DISPONIVEL'),
             ('SUV', 'Hyundai', 'Creta', 2023, 'RST6U78', 'Azul', 28900, 'MANUTENCAO')
     ) AS dados(
                categoria_nome,
                marca,
                modelo,
                ano,
                placa,
                cor,
                quilometragem,
                status
    )
         JOIN categorias_veiculos c
              ON c.nome = dados.categoria_nome;

-- ============================================================
-- RESERVAS
-- Dados de teste das reservas realizadas pelos clientes.
-- ============================================================

INSERT INTO reservas (
    cliente_id,
    categoria_id,
    data_inicio,
    data_fim,
    status,
    observacoes
)
SELECT
    c.id,
    cv.id,
    dados.data_inicio,
    dados.data_fim,
    dados.status,
    dados.observacoes
FROM (
         VALUES
             (
                 '12345678901',
                 'Economico',
                 '2026-09-15 08:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-18 08:00:00-03:00'::TIMESTAMPTZ,
                 'CONFIRMADA',
                 'Reserva para viagem de trabalho.'
             ),
             (
                 '23456789012',
                 'SUV',
                 '2026-09-20 10:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-25 10:00:00-03:00'::TIMESTAMPTZ,
                 'PENDENTE',
                 'Cliente solicitou veículo com maior espaço.'
             ),
             (
                 '34567890123',
                 'Intermediario',
                 '2026-09-05 09:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-08 09:00:00-03:00'::TIMESTAMPTZ,
                 'CONCLUIDA',
                 'Reserva já finalizada.'
             ),
             (
                 '12345678901',
                 'SUV',
                 '2026-09-10 08:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-12 08:00:00-03:00'::TIMESTAMPTZ,
                 'CANCELADA',
                 'Reserva cancelada pelo cliente.'
             )
     ) AS dados(
                cpf_cliente,
                categoria_nome,
                data_inicio,
                data_fim,
                status,
                observacoes
    )
         JOIN clientes c
              ON c.cpf = dados.cpf_cliente
         JOIN categorias_veiculos cv
              ON cv.nome = dados.categoria_nome;

-- ============================================================
-- LOCACOES
-- Dados de teste das locações realizadas.
-- ============================================================

INSERT INTO locacoes (
    reserva_id,
    cliente_id,
    veiculo_id,
    inicio,
    previsao_devolucao,
    devolucao,
    valor_diaria,
    valor_total,
    status
)
SELECT
    r.id,
    c.id,
    v.id,
    dados.inicio,
    dados.previsao_devolucao,
    dados.devolucao,
    cv.valor_diaria,
    dados.valor_total,
    dados.status
FROM (
         VALUES
             (
                 '12345678901',
                 'ABC1D23',
                 '2026-09-15 08:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-18 08:00:00-03:00'::TIMESTAMPTZ,
                 NULL::TIMESTAMPTZ,
                 269.70,
                 'ATIVA'
             ),
             (
                 '34567890123',
                 'GHI7J89',
                 '2026-09-05 09:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-08 09:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-08 08:30:00-03:00'::TIMESTAMPTZ,
                 389.70,
                 'CONCLUIDA'
             )
     ) AS dados(
                cpf_cliente,
                placa_veiculo,
                inicio,
                previsao_devolucao,
                devolucao,
                valor_total,
                status
    )
         JOIN clientes c
              ON c.cpf = dados.cpf_cliente
         JOIN veiculos v
              ON v.placa = dados.placa_veiculo
         JOIN categorias_veiculos cv
              ON cv.id = v.categoria_id
         LEFT JOIN reservas r
                   ON r.cliente_id = c.id
                       AND r.categoria_id = v.categoria_id
                       AND r.data_inicio = dados.inicio
                       AND r.status = 'CONFIRMADA';

-- ============================================================
-- SESSOES
-- Dados de teste das sessões dos usuários.
-- ============================================================

INSERT INTO sessoes (
    usuario_id,
    inicio,
    fim,
    ip_origem,
    user_agent,
    status
)
SELECT
    u.id,
    dados.inicio,
    dados.fim,
    dados.ip_origem,
    dados.user_agent,
    dados.status
FROM (
         VALUES
             (
                 'admin@locadora.com',
                 '2026-09-11 08:00:00-03:00'::TIMESTAMPTZ,
                 '2026-09-11 10:30:00-03:00'::TIMESTAMPTZ,
                 '127.0.0.1',
                 'Mozilla/5.0',
                 'ENCERRADA'
             ),
             (
                 'atendente@locadora.com',
                 '2026-09-11 08:10:00-03:00'::TIMESTAMPTZ,
                 NULL::TIMESTAMPTZ,
                 '127.0.0.1',
                 'Mozilla/5.0',
                 'ATIVA'
             )
     ) AS dados(
                email_usuario,
                inicio,
                fim,
                ip_origem,
                user_agent,
                status
    )
         JOIN usuarios u
              ON u.email = dados.email_usuario;

-- ============================================================
-- LOGS
-- Dados de teste dos eventos registrados pelo sistema.
-- ============================================================

INSERT INTO logs (
    usuario_id,
    sessao_id,
    tipo,
    nivel,
    acao,
    mensagem,
    data_hora
)
SELECT
    u.id,
    s.id,
    dados.tipo,
    dados.nivel,
    dados.acao,
    dados.mensagem,
    dados.data_hora
FROM (
         VALUES
             (
                 'admin@locadora.com',
                 '2026-09-11 08:00:00-03:00'::TIMESTAMPTZ,
                 'USO',
                 'INFO',
                 'LOGIN',
                 'Administrador realizou login no sistema.',
                 '2026-09-11 08:00:05-03:00'::TIMESTAMPTZ
             ),
             (
                 'admin@locadora.com',
                 '2026-09-11 08:00:00-03:00'::TIMESTAMPTZ,
                 'USO',
                 'INFO',
                 'CRIAR_RESERVA',
                 'Administrador registrou uma nova reserva.',
                 '2026-09-11 08:25:00-03:00'::TIMESTAMPTZ
             ),
             (
                 'atendente@locadora.com',
                 '2026-09-11 08:10:00-03:00'::TIMESTAMPTZ,
                 'USO',
                 'INFO',
                 'LOGIN',
                 'Atendente realizou login no sistema.',
                 '2026-09-11 08:10:05-03:00'::TIMESTAMPTZ
             ),
             (
                 'atendente@locadora.com',
                 '2026-09-11 08:10:00-03:00'::TIMESTAMPTZ,
                 'USO',
                 'INFO',
                 'CRIAR_LOCACAO',
                 'Atendente registrou uma nova locação.',
                 '2026-09-11 08:40:00-03:00'::TIMESTAMPTZ
             ),
             (
                 'atendente@locadora.com',
                 '2026-09-11 08:10:00-03:00'::TIMESTAMPTZ,
                 'ERRO',
                 'WARN',
                 'VEICULO_INDISPONIVEL',
                 'Tentativa de realizar locação de um veículo indisponível.',
                 '2026-09-11 09:15:00-03:00'::TIMESTAMPTZ
             )
     ) AS dados(
                email_usuario,
                inicio_sessao,
                tipo,
                nivel,
                acao,
                mensagem,
                data_hora
    )
         JOIN usuarios u
              ON u.email = dados.email_usuario
         JOIN sessoes s
              ON s.usuario_id = u.id
                  AND s.inicio = dados.inicio_sessao;