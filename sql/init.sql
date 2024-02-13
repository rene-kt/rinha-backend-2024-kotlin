CREATE TABLE clientes(
    id SERIAL PRIMARY KEY,
    limite INT,
    saldo INT
);

CREATE TABLE transacoes(
    id SERIAL PRIMARY KEY,
    valor INT,
    tipo CHAR(1),
    descricao VARCHAR(255),
    data_criacao TIMESTAMP,
    cliente_id INT REFERENCES clientes(id)
);

INSERT INTO clientes (limite, saldo)
  VALUES
    (1000 * 100, 0),
    (800 * 100, 0),
    (10000 * 100, 0),
    (100000 * 100, 0),
    (5000 * 100, 0);