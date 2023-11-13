package com.example.common.dao;

import com.example.common.model.Equipe;
import com.example.common.model.Pessoa;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquipeDAO {

    public void inserirEquipe(Equipe equipe) {
        String sqlEquipe = "INSERT INTO Equipe (nome) VALUES (?)";
        String sqlPessoaEquipe = "INSERT INTO Pessoa_has_Equipe (Pessoa_ID_Pessoa, Equipe_ID_Equipe) VALUES (?, ?)";

        Connection conexao = null;
        PreparedStatement stmtEquipe = null;
        PreparedStatement stmtPessoaEquipe = null;
        ResultSet generatedKeys = null;

        try {
            conexao = ConexaoBancoDados.abrirConexao();
            conexao.setAutoCommit(false); // Desabilita o auto-commit para controle manual de transação

            // Insere a equipe
            stmtEquipe = conexao.prepareStatement(sqlEquipe, Statement.RETURN_GENERATED_KEYS);
            stmtEquipe.setString(1, equipe.getNome());
            stmtEquipe.executeUpdate();

            // Obtém o ID da equipe inserida
            generatedKeys = stmtEquipe.getGeneratedKeys();
            int idEquipe = 0;
            if (generatedKeys.next()) {
                idEquipe = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Criação da equipe falhou, nenhum ID obtido.");
            }

            // Associa cada membro à equipe
            stmtPessoaEquipe = conexao.prepareStatement(sqlPessoaEquipe);
            for (Pessoa membro : equipe.getMembros()) {
                stmtPessoaEquipe.setInt(1, membro.getID_Pessoa());
                stmtPessoaEquipe.setInt(2, idEquipe);
                stmtPessoaEquipe.executeUpdate();
            }

            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            // Fecha as conexões e statements
            if (generatedKeys != null) try {
                generatedKeys.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (stmtEquipe != null) try {
                stmtEquipe.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (stmtPessoaEquipe != null) try {
                stmtPessoaEquipe.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    //GPT
    public void atualizarEquipe(Equipe equipe) {
        Connection conexao = null;
        Set<Integer> membrosExistentes = new HashSet<>();

        try {
            conexao = ConexaoBancoDados.abrirConexao();
            conexao.setAutoCommit(false);

            System.out.println("Iniciando atualização da equipe: " + equipe.getNome());

            // Atualizar nome da equipe (se necessário)
            String sqlUpdateEquipe = "UPDATE Equipe SET nome = ? WHERE ID_Equipe = ?";
            try ( PreparedStatement stmtUpdateEquipe = conexao.prepareStatement(sqlUpdateEquipe)) {
                stmtUpdateEquipe.setString(1, equipe.getNome());
                stmtUpdateEquipe.setInt(2, equipe.getID_Equipe());
                int rowsAffected = stmtUpdateEquipe.executeUpdate();
                System.out.println("Nome da equipe atualizado, linhas afetadas: " + rowsAffected);
            }

            // Identificar membros existentes
            String sqlSelectMembros = "SELECT Pessoa_ID_Pessoa FROM Pessoa_has_Equipe WHERE Equipe_ID_Equipe = ?";
            try ( PreparedStatement stmtSelectMembros = conexao.prepareStatement(sqlSelectMembros)) {
                stmtSelectMembros.setInt(1, equipe.getID_Equipe());
                ResultSet rs = stmtSelectMembros.executeQuery();
                while (rs.next()) {
                    membrosExistentes.add(rs.getInt("Pessoa_ID_Pessoa"));
                }
            }
            System.out.println("Membros existentes antes da atualização: " + membrosExistentes);

            // Atualizar membros
            String sqlInsertMembro = "INSERT INTO Pessoa_has_Equipe (Pessoa_ID_Pessoa, Equipe_ID_Equipe) VALUES (?, ?)";
            String sqlDeleteMembro = "DELETE FROM Pessoa_has_Equipe WHERE Pessoa_ID_Pessoa = ? AND Equipe_ID_Equipe = ?";
            try ( PreparedStatement stmtInsertMembro = conexao.prepareStatement(sqlInsertMembro);  PreparedStatement stmtDeleteMembro = conexao.prepareStatement(sqlDeleteMembro)) {

                for (Pessoa membro : equipe.getMembros()) {
                    if (!membrosExistentes.contains(membro.getID_Pessoa())) {
                        stmtInsertMembro.setInt(1, membro.getID_Pessoa());
                        stmtInsertMembro.setInt(2, equipe.getID_Equipe());
                        stmtInsertMembro.executeUpdate();
                        System.out.println("Membro adicionado: " + membro.getID_Pessoa());
                    }
                    membrosExistentes.remove(membro.getID_Pessoa());
                }

                for (Integer idPessoa : membrosExistentes) {
                    stmtDeleteMembro.setInt(1, idPessoa);
                    stmtDeleteMembro.setInt(2, equipe.getID_Equipe());
                    stmtDeleteMembro.executeUpdate();
                    System.out.println("Membro removido: " + idPessoa);
                }
            }

            conexao.commit();
            System.out.println("Equipe atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro na atualização da equipe: " + e.getMessage());
            try {
                if (conexao != null) {
                    conexao.rollback();
                    System.out.println("Rollback realizado devido a erro.");
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao executar rollback: " + ex.getMessage());
            }
        } finally {
            if (conexao != null) {
                try {
                    conexao.close();
                    System.out.println("Conexão fechada.");
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public void excluirEquipe(int idEquipe) {
        String sql = "DELETE FROM Equipe WHERE ID_equipe=?";

        try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public Equipe obterEquipePorId(int idEquipe) {
        String sql = "SELECT * FROM Equipe WHERE ID_equipe=?";
        Equipe equipe = null;

        try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                String nome = resultado.getString("nome");
                equipe = new Equipe(idEquipe, nome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }

        return equipe;
    }

    public List<Equipe> listarTodasEquipes() {
        List<Equipe> listaEquipes = new ArrayList<>();
        String sql = "SELECT * FROM Equipe";

        try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql);  ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int idEquipe = resultado.getInt("ID_equipe");
                String nome = resultado.getString("nome");
                Equipe equipe = new Equipe(idEquipe, nome);
                listaEquipes.add(equipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }

        return listaEquipes;
    }

    public List<Pessoa> listarMembrosDaEquipe(int idEquipe) {
        String sql = "SELECT p.* from Pessoa p JOIN Pessoa_has_equipe pe ON p.ID_Pessoa = pe.Pessoa_ID_Pessoa WHERE pe.Equipe_ID_Equipe = ?";
        List<Pessoa> membros = new ArrayList<>();
        try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setID_Pessoa(rs.getInt("ID_Pessoa"));
                pessoa.setCpf(rs.getString("cpf"));
                pessoa.setUsuario(rs.getString("Usuario"));
                pessoa.setEmail(rs.getString("Email"));
                pessoa.setSenha(rs.getString("Senha"));
                pessoa.setTipo(rs.getInt("Tipo"));

                membros.add(pessoa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
        return membros;
    }

    public List<Equipe> obterEquipePorPessoaId(int idPessoa) {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT e.* FROM Equipe e JOIN Pessoa_has_Equipe pe ON e.ID_Equipe = pe.Equipe_ID_Equipe WHERE pe.Pessoa_ID_Pessoa = ?";
        try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idEquipe = rs.getInt("ID_Equipe");
                    String nome = rs.getString("nome");
                    Equipe equipe = new Equipe(idEquipe, nome);
                    equipes.add(equipe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
        return equipes;
    }

}
