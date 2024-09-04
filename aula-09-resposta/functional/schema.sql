CREATE TABLE musica (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    artista VARCHAR(255) NOT NULL,
    tamanhoMinutos DECIMAL(5,2) NOT NULL
);