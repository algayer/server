package com.example.common.dao;

import com.example.common.model.Anexo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnexoDAO {

    public void inserirAnexo(Anexo anexo) {
        String sql = "INSERT INTO Anexos (Nome, Dados, ID_Tarefa) VALUES (?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, anexo.getNome());
            stmt.setBytes(2, anexo.getDados());
            stmt.setInt(3, anexo.getID_Tarefa());
            stmt.executeUpdate();
            ConexaoBancoDados.commit();
        } catch (SQLException e) {
            ConexaoBancoDados.rollback();
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
    }

    public void atualizarAnexo(Anexo anexo) {
        String sql = "UPDATE Anexos SET Nome=?, Dados=?, ID_Tarefa=? WHERE ID_Anexo=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, anexo.getNome());
            stmt.setBytes(2, anexo.getDados());
            stmt.setInt(3, anexo.getID_Tarefa());
            stmt.setInt(4, anexo.getID_Anexo());
            stmt.executeUpdate();
            ConexaoBancoDados.commit();
        } catch (SQLException e) {
            ConexaoBancoDados.rollback();
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
    }

    public Anexo obterAnexoPorId(int idAnexo) {
        String sql = "SELECT * FROM Anexos WHERE ID_Anexo=?";
        Anexo anexo = null;

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idAnexo);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                String nome = resultado.getString("Nome");
                byte[] dados = resultado.getBytes("Dados");
                int idTarefa = resultado.getInt("ID_Tarefa");
                anexo = new Anexo(idAnexo, nome, dados, idTarefa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }

        return anexo;
    }

    public List<Anexo> listarTodosAnexos() {
        List<Anexo> listaAnexos = new ArrayList<>();
        String sql = "SELECT * FROM Anexos";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int idAnexo = resultado.getInt("ID_Anexo");
                String nome = resultado.getString("Nome");
                byte[] dados = resultado.getBytes("Dados");
                int idTarefa = resultado.getInt("ID_Tarefa");
                Anexo anexo = new Anexo(idAnexo, nome, dados, idTarefa);
                listaAnexos.add(anexo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }

        return listaAnexos;
    }

    public List<Anexo> listarAnexosPorTarefa(int idTarefa) {
        List<Anexo> listaAnexos = new ArrayList<>();
        String sql = "SELECT * FROM Anexos WHERE ID_Tarefa=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idTarefa);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                int idAnexo = resultado.getInt("ID_Anexo");
                String nome = resultado.getString("Nome");
                byte[] dados = resultado.getBytes("Dados");
                Anexo anexo = new Anexo(idAnexo, nome, dados, idTarefa);
                listaAnexos.add(anexo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }

        return listaAnexos;
    }

    public void excluirAnexo(int idAnexo) {
        String sql = "DELETE FROM Anexos WHERE ID_Anexo=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idAnexo);
            stmt.executeUpdate();
            ConexaoBancoDados.commit();
        } catch (SQLException e) {
            ConexaoBancoDados.rollback();
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao();
        }
    }
}
