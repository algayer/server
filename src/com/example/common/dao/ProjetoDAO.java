package com.example.common.dao;

import com.example.common.model.Projeto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    public void inserirProjeto(Projeto projeto) {
        String sql = "INSERT INTO Projeto (nome, Descricao, DataEntrega, DataInicial) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataEntrega().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataInicial().getTime()));

            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public void atualizarProjeto(Projeto projeto) {
        String sql = "UPDATE Projeto SET nome=?, Descricao=?, DataEntrega=?, DataInicial=? WHERE ID_Projeto=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataEntrega().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataInicial().getTime()));

            stmt.setInt(5, projeto.getID_Projeto());
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public void excluirProjeto(int idProjeto) {
        String sql = "DELETE FROM Projeto WHERE ID_Projeto=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idProjeto);
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public Projeto obterProjetoPorId(int idProjeto) {
        String sql = "SELECT * FROM Projeto WHERE ID_Projeto=?";
        Projeto projeto = null;

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idProjeto);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                String nome = resultado.getString("nome");
                String descricao = resultado.getString("Descricao");
                java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
                java.sql.Date dataInicial = resultado.getDate("DataInicial");
                int ID_Equipe = resultado.getInt("ID_Equipe");
                projeto = new Projeto(idProjeto, nome, descricao, dataEntrega, dataInicial, ID_Equipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }

        return projeto;
    }

    public List<Projeto> listarTodosProjetos() {
        List<Projeto> listaProjetos = new ArrayList<>();
        String sql = "SELECT * FROM Projeto";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int idProjeto = resultado.getInt("ID_Projeto");
                String nome = resultado.getString("nome");
                String descricao = resultado.getString("Descricao");
                java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
                java.sql.Date dataInicial = resultado.getDate("DataInicial");
                int ID_Equipe = resultado.getInt("ID_Equipe");
                Projeto projeto = new Projeto(idProjeto, nome, descricao, dataEntrega, dataInicial, ID_Equipe);
                listaProjetos.add(projeto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }

        return listaProjetos;
    }
}

