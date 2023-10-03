package com.example.common.dao;

import com.example.common.model.Equipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    public void inserirEquipe(Equipe equipe) {
        String sql = "INSERT INTO Equipe (nome) VALUES (?)";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, equipe.getNome());
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public void atualizarEquipe(Equipe equipe) {
        String sql = "UPDATE Equipe SET nome=? WHERE ID_equipe=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
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

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
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

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
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

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet resultado = stmt.executeQuery()) {
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

