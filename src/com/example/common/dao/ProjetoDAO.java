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
        String sql = "INSERT INTO Projeto (nome, Descricao, DataEntrega, DataInicial, ID_Equipe, Estado) VALUES (?, ?, ?, ?,?,?)";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataEntrega().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataInicial().getTime()));
            stmt.setInt(5, projeto.getID_Equipe());
            stmt.setBoolean(6, projeto.getEstado());
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
        String sql = "UPDATE Projeto SET nome=?, Descricao=?, DataEntrega=?, DataInicial=?, Estado=? WHERE ID_Projeto=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataEntrega().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataInicial().getTime()));
            stmt.setBoolean(5, projeto.getEstado());
            stmt.setInt(6, projeto.getID_Projeto());
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

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
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

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idProjeto);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                String nome = resultado.getString("nome");
                String descricao = resultado.getString("Descricao");
                java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
                java.sql.Date dataInicial = resultado.getDate("DataInicial");
                int ID_Equipe = resultado.getInt("ID_Equipe");
                boolean Estado = resultado.getBoolean("Estado");
                projeto = new Projeto(idProjeto, nome, descricao, dataEntrega, dataInicial, ID_Equipe, Estado);
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
        String sqlProjetos = "SELECT p.ID_Projeto, p.nome AS nome_projeto, p.Descricao, p.DataEntrega, p.DataInicial, p.ID_Equipe, p.Estado FROM Projeto p";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmtProjetos = conexao.prepareStatement(sqlProjetos); ResultSet resultadoProjetos = stmtProjetos.executeQuery()) {

            while (resultadoProjetos.next()) {
                int idProjeto = resultadoProjetos.getInt("ID_Projeto");
                String nomeProjeto = resultadoProjetos.getString("nome_projeto");
                String descricao = resultadoProjetos.getString("Descricao");
                java.sql.Date dataEntrega = resultadoProjetos.getDate("DataEntrega");
                java.sql.Date dataInicial = resultadoProjetos.getDate("DataInicial");
                int idEquipe = resultadoProjetos.getInt("ID_Equipe");
                boolean Estado = resultadoProjetos.getBoolean("Estado");

                Projeto projeto = new Projeto(idProjeto, nomeProjeto, descricao, dataEntrega, dataInicial, idEquipe, Estado);

                // consulta para obter o nome da equipe com base no ID_Equipe
                String sqlNomeEquipe = "SELECT Nome FROM Equipe WHERE ID_Equipe = ?";
                try (PreparedStatement stmtNomeEquipe = conexao.prepareStatement(sqlNomeEquipe)) {
                    stmtNomeEquipe.setInt(1, idEquipe);
                    ResultSet resultadoNomeEquipe = stmtNomeEquipe.executeQuery();
                    if (resultadoNomeEquipe.next()) {
                        String nomeEquipe = resultadoNomeEquipe.getString("Nome");
                        projeto.setNomeEquipe(nomeEquipe);
                    }
                }

                listaProjetos.add(projeto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProjetos;
    }

    public ArrayList<Projeto> getProjetosPorNome(String nome) {
        ArrayList<Projeto> listaProjetosPorNome = new ArrayList<>();
        String sql = "SELECT p.ID_Projeto, p.nome AS nome_projeto, p.Descricao, p.DataEntrega, p.DataInicial, p.ID_Equipe, p.Estado FROM Projeto p WHERE p.nome LIKE ?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                int idProjeto = resultado.getInt("ID_Projeto");
                String nomeProjeto = resultado.getString("nome_projeto");
                String descricao = resultado.getString("Descricao");
                java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
                java.sql.Date dataInicial = resultado.getDate("DataInicial");
                int idEquipe = resultado.getInt("ID_Equipe");
                boolean Estado = resultado.getBoolean("Estado");

                Projeto projeto = new Projeto(idProjeto, nomeProjeto, descricao, dataEntrega, dataInicial, idEquipe, Estado);

                String sqlNomeEquipe = "SELECT Nome FROM Equipe WHERE ID_Equipe = ?";
                try (PreparedStatement stmtNomeEquipe = conexao.prepareStatement(sqlNomeEquipe)) {
                    stmtNomeEquipe.setInt(1, idEquipe);
                    ResultSet resultadoNomeEquipe = stmtNomeEquipe.executeQuery();
                    if (resultadoNomeEquipe.next()) {
                        String nomeEquipe = resultadoNomeEquipe.getString("Nome");
                        projeto.setNomeEquipe(nomeEquipe);
                    }
                }
                listaProjetosPorNome.add(projeto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProjetosPorNome;
    }
    
    public List<Projeto> listarProjetosPorEquipe(int idEquipe) {
    List<Projeto> listaProjetos = new ArrayList<>();
    String sql = "SELECT * FROM Projeto WHERE ID_Equipe=?";

    try ( Connection conexao = ConexaoBancoDados.abrirConexao();  PreparedStatement stmt = conexao.prepareStatement(sql)) {

        stmt.setInt(1, idEquipe);
        ResultSet resultado = stmt.executeQuery();

        while (resultado.next()) {
            int idProjeto = resultado.getInt("ID_Projeto");
            String nomeProjeto = resultado.getString("Nome");
            String descricao = resultado.getString("Descricao");
            java.sql.Date dataEntrega = resultado.getDate("DataEntrega");
            java.sql.Date dataInicial = resultado.getDate("DataInicial");
            boolean Estado = resultado.getBoolean("Estado");

            Projeto projeto = new Projeto(idProjeto, nomeProjeto, descricao, dataEntrega, dataInicial, idEquipe, Estado);
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


