-- Execute em bancos existentes antes de iniciar a versão com auditoria HTTP.
ALTER TABLE logs
    ADD COLUMN IF NOT EXISTS metodo_http VARCHAR(10),
    ADD COLUMN IF NOT EXISTS url VARCHAR(2048),
    ADD COLUMN IF NOT EXISTS ip_cliente VARCHAR(45),
    ADD COLUMN IF NOT EXISTS status_http INTEGER,
    ADD COLUMN IF NOT EXISTS payload TEXT,
    ADD COLUMN IF NOT EXISTS headers TEXT;

CREATE INDEX IF NOT EXISTS idx_logs_metodo_data
    ON logs (metodo_http, data_hora);
