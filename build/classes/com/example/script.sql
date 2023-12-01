-- Excluindo e criando o esquema
DROP SCHEMA IF EXISTS TrabalhoFinal;
CREATE SCHEMA IF NOT EXISTS TrabalhoFinal DEFAULT CHARACTER SET utf8;

-- Definindo o esquema padrão como TrabalhoFinal
USE TrabalhoFinal;

-- Excluindo e criando a tabela Pessoa
DROP TABLE IF EXISTS Pessoa;
CREATE TABLE IF NOT EXISTS Pessoa (
  ID_Pessoa INT NOT NULL AUTO_INCREMENT,
  cpf VARCHAR(11) NOT NULL,
  Usuario VARCHAR(45) NOT NULL,
  Email VARCHAR(60),
  Senha VARCHAR(100) NOT NULL,
  Tipo INT NOT NULL, -- 0 para gerente, 1 para funcionário
  PRIMARY KEY (ID_Pessoa)
);

-- Excluindo e criando a tabela Equipe
DROP TABLE IF EXISTS Equipe;
CREATE TABLE IF NOT EXISTS Equipe (
  ID_Equipe INT NOT NULL AUTO_INCREMENT,
  Nome VARCHAR(45) NOT NULL,
  PRIMARY KEY (ID_Equipe)
);

-- Excluindo e criando a tabela Projeto
DROP TABLE IF EXISTS Projeto;
CREATE TABLE IF NOT EXISTS Projeto (
  ID_Projeto INT NOT NULL AUTO_INCREMENT,
  Nome VARCHAR(45) NOT NULL,
  Descricao VARCHAR(45) NOT NULL,
  DataEntrega DATE NOT NULL,
  DataInicial DATE NOT NULL,
  ID_Equipe INT NOT NULL,
  Estado BOOLEAN NOT NULL,
  PRIMARY KEY (ID_Projeto),
  FOREIGN KEY (ID_Equipe) REFERENCES Equipe (ID_Equipe)
);

-- Excluindo e criando a tabela Tarefa
DROP TABLE IF EXISTS Tarefa;
CREATE TABLE IF NOT EXISTS Tarefa (
  ID_Tarefa INT NOT NULL AUTO_INCREMENT,
  Nome VARCHAR(45) NOT NULL,
  Descricao VARCHAR(45) NOT NULL,
  HorasTrabalhadas VARCHAR(15) NULL,
  DataEntrega Date NOT NULL,
  ID_Projeto INT NOT NULL,
  Estado BOOLEAN NOT NULL,
  PRIMARY KEY (ID_Tarefa),
  FOREIGN KEY (ID_Projeto) REFERENCES Projeto (ID_Projeto));

-- Excluindo e criando a tabela Pessoa_has_Equipe
DROP TABLE IF EXISTS Pessoa_has_Equipe;
CREATE TABLE IF NOT EXISTS Pessoa_has_Equipe (
  Pessoa_ID_Pessoa INT NOT NULL,
  Equipe_ID_Equipe INT NOT NULL,
  PRIMARY KEY (Pessoa_ID_Pessoa, Equipe_ID_Equipe),
  FOREIGN KEY (Pessoa_ID_Pessoa) REFERENCES Pessoa (ID_Pessoa),
  FOREIGN KEY (Equipe_ID_Equipe) REFERENCES Equipe (ID_Equipe)
);

-- Criando a tabela de Anexos associada às Tarefas
CREATE TABLE IF NOT EXISTS Anexos (
  ID_Anexo INT AUTO_INCREMENT PRIMARY KEY,
  Nome VARCHAR(255),
  Dados LONGBLOB,
  ID_Tarefa INT,
  FOREIGN KEY (ID_Tarefa) REFERENCES Tarefa(ID_Tarefa)
);

-- Criando a tabela intermediária para mapear tarefas e anexos
CREATE TABLE IF NOT EXISTS Tarefa_Anexos (
  ID_Tarefa INT NOT NULL,
  ID_Anexo INT NOT NULL,
  PRIMARY KEY (ID_Tarefa, ID_Anexo),
  FOREIGN KEY (ID_Tarefa) REFERENCES Tarefa(ID_Tarefa),
  FOREIGN KEY (ID_Anexo) REFERENCES Anexos(ID_Anexo)
);

-- Inserindo um novo usuário na tabela Pessoa
INSERT INTO Pessoa (cpf, Usuario, Email, Senha, Tipo)
VALUES ('03426608022', 'Algayer', 'matheusalgayer15@gmail.com', '12345', 1),
('11122233345', 'Maninho', 'wellington@gmail.com','5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5',1),
('12345678901', 'Maria Silva', 'maria.silva@email.com', 'senha123', 0),
('98765432102', 'João Pereira', 'joao.pereira@email.com', 'senha456', 0),
('56789012303', 'Ana Souza', 'ana.souza@email.com', 'senha789', 0),
('23456789004', 'Pedro Santos', 'pedro.santos@email.com', 'senha012', 0),
('89012345605', 'Carla Lima', 'carla.lima@email.com', 'senha345', 0),
('34567890106', 'Lucas Oliveira', 'lucas.oliveira@email.com', 'senha678', 0),
('90123456707', 'Mariana Costa', 'mariana.costa@email.com', 'senha901', 0),
('45678901208', 'Rafael Fernandes', 'rafael.fernandes@email.com', 'senha234', 0);

INSERT INTO Equipe (Nome)
VALUES ('Equipe Alpha'), ('Equipe Beta'), ('Equipe Gamma');

INSERT INTO Pessoa_has_Equipe (Pessoa_ID_Pessoa, Equipe_ID_Equipe)
VALUES ((SELECT ID_Pessoa FROM Pessoa WHERE Usuario = 'Algayer'), 1);

INSERT INTO Projeto (Nome, Descricao, DataEntrega, DataInicial, ID_Equipe, Estado)
VALUES ('Projeto 1', 'Desenvolvimento de um novo produto', '2023-12-31', '2023-01-01', 1, FALSE),
       ('Projeto 2', 'Pesquisa de mercado', '2023-06-30', '2023-02-01', 1, FALSE);

INSERT INTO Tarefa (Nome, Descricao, HorasTrabalhadas, DataEntrega, ID_Projeto, Estado)
VALUES ('Análise de requisitos', 'Analisar os requisitos do produto', NULL, '2023-03-01', 1, FALSE),
       ('Design do produto', 'Desenvolver o design do produto', NULL, '2023-04-01', 1, FALSE),
       ('Estudo de mercado', 'Realizar um estudo de mercado', NULL, '2023-03-15', 2, FALSE),
       ('Estudo de mercado', 'Realizar um estudo de mercado', "15:00:00", '2023-03-15', 2, TRUE),
       ('Análise de concorrência', 'Analisar os concorrentes', NULL, '2023-04-15', 2, FALSE);
       


