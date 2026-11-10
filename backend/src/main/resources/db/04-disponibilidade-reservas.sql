-- Índices usados na verificação transacional de disponibilidade por categoria/período.
CREATE INDEX IF NOT EXISTS idx_reservas_categoria_periodo_status
    ON reservas (categoria_id, status, data_inicio, data_fim);

CREATE INDEX IF NOT EXISTS idx_locacoes_veiculo_periodo_status
    ON locacoes (veiculo_id, status, inicio, previsao_devolucao);
