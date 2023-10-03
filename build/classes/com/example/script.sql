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
  Email VARCHAR(45),
  Senha VARCHAR(45) NOT NULL,
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
  PRIMARY KEY (ID_Projeto),
  FOREIGN KEY (ID_Equipe) REFERENCES Equipe (ID_Equipe)
);

-- Excluindo e criando a tabela Tarefa
DROP TABLE IF EXISTS Tarefa;
CREATE TABLE IF NOT EXISTS Tarefa (
  ID_Tarefa INT NOT NULL AUTO_INCREMENT,
  Nome VARCHAR(45) NOT NULL,
  Descricao VARCHAR(45) NOT NULL,
  HorasTrabalhadas TIME NOT NULL,
  ID_Projeto INT NOT NULL,
  ID_Pessoa INT NOT NULL,
  PRIMARY KEY (ID_Tarefa),
  FOREIGN KEY (ID_Projeto) REFERENCES Projeto (ID_Projeto),
  FOREIGN KEY (ID_Pessoa) REFERENCES Pessoa (ID_Pessoa)
);

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
VALUES ('03426608022', 'Algayer', 'matheusalgayer15@gmail.com', 'Senha14051979', 1);

