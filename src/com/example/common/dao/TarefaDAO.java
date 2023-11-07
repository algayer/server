package com.example.common.dao;

import com.example.common.model.Tarefa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    public void inserirTarefa(Tarefa tarefa) {
        String sql = "INSERT INTO Tarefa (Nome, Descricao, HorasTrabalhadas,DataEntrega, ID_Projeto, Estado) VALUES (?, ?, ?, ?, ?,?)";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getNome());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setTime(3,tarefa.getHorasTrabalhadas());
            stmt.setDate(4, new java.sql.Date(tarefa.getDataEntrega().getTime()));
            stmt.setInt(5, tarefa.getID_Projeto());
            stmt.setBoolean(6, tarefa.getEstado());

            stmt.executeUpdate();
            ConexaoBancoDados.commit();
        } catch (SQLException e) {
            ConexaoBancoDados.rollback();
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
    }

    public void atualizarTarefa(Tarefa tarefa) {
        String sql = "UPDATE Tarefa SET Nome=?, Descricao=?, HorasTrabalhadas=?, DataEntrega=?, ID_Projeto=?, Estado=? WHERE ID_Tarefa=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getNome());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setTime(3, tarefa.getHorasTrabalhadas());
            stmt.setDate(4, new java.sql.Date(tarefa.getDataEntrega().getTime()));
            stmt.setInt(5, tarefa.getID_Projeto());
            stmt.setBoolean(6, tarefa.getEstado());
            stmt.setInt(7, tarefa.getID_Tarefa());
            stmt.executeUpdate();
            ConexaoBancoDados.commit();
        } catch (SQLException e) {
            ConexaoBancoDados.rollback();
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
    }

    public void excluirTarefa(int idTarefa) {
        String sql = "DELETE FROM Tarefa WHERE ID_Tarefa=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idTarefa);
            stmt.executeUpdate();
            ConexaoBancoDados.commit();
        } catch (SQLException e) {
            ConexaoBancoDados.rollback();
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
    }

    public Tarefa obterTarefaPorId(int idTarefa) {
        String sql = "SELECT * FROM Tarefa WHERE ID_Tarefa=?";
        Tarefa tarefa = null;

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idTarefa);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                String nome = resultado.getString("Nome");
                String descricao = resultado.getString("Descricao");
                Time horasTrabalhadas = resultado.getTime("HorasTrabalhadas");
                java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
                int idProjeto = resultado.getInt("ID_Projeto");
                boolean Estado = resultado.getBoolean("Estado");
                tarefa = new Tarefa(idTarefa, nome, descricao, horasTrabalhadas, idProjeto, dataEntrega, Estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }

        return tarefa;
    }

    public List<Tarefa> listarTodasTarefas() {
        List<Tarefa> listaTarefas = new ArrayList<>();
        String sql = "SELECT * FROM Tarefa";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int idTarefa = resultado.getInt("ID_Tarefa");
                String nome = resultado.getString("Nome");
                String descricao = resultado.getString("Descricao");
                Time horasTrabalhadas = resultado.getTime("HorasTrabalhadas");
                java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
                int idProjeto = resultado.getInt("ID_Projeto");
                boolean Estado = resultado.getBoolean("Estado");
                Tarefa tarefa = new Tarefa(idTarefa, nome, descricao, horasTrabalhadas, idProjeto, dataEntrega, Estado);
                listaTarefas.add(tarefa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }

        return listaTarefas;
    }
}
