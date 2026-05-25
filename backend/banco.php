USE engenharia_191;

CREATE TABLE jogadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    idade INT NOT NULL,
    email VARCHAR(150) NOT NULL,
    pontos INT DEFAULT 0
);