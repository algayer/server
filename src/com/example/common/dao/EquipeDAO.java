package com.example.common.dao;

import com.example.common.model.Equipe;
import com.example.common.model.Pessoa;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public void atualizarEquipe(Equipe equipe) {
        String sql = "UPDATE Equipe SET nome=? WHERE ID_equipe=?";

        try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, equipe.getNome());
            stmt.setInt(2, equipe.getID_Equipe());
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
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
}
