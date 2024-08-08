CREATE TABLE TABLE_ROUNDS (
    id UUID PRIMARY KEY,
    id_partida UUID NOT NULL,
    id_jogador_vencedor UUID,
    status VARCHAR(20) NOT NULL,
    valor_pote DECIMAL(10, 2),
    current_player_id UUID,
    current_amount DECIMAL(10, 2),
    dt_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_partida) REFERENCES TABLE_MATCHES(id),
    FOREIGN KEY (id_jogador_vencedor) REFERENCES TABLE_PLAYERS(id),
    FOREIGN KEY (current_player_id) REFERENCES TABLE_PLAYERS(id)
);