package com.example.common.dao;

import com.example.common.model.Pessoa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {
    public void inserirPessoa(Pessoa pessoa) {
        String sql = "INSERT INTO Pessoa (cpf, Usuario, Email, Senha, Tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getCpf());
            stmt.setString(2, pessoa.getUsuario());
            stmt.setString(3, pessoa.getEmail());
            stmt.setString(4, pessoa.getSenha());
            stmt.setInt(5, pessoa.getTipo());
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public void atualizarPessoa(Pessoa pessoa) {
        String sql = "UPDATE Pessoa SET cpf=?, Usuario=?, Email=?, Senha=?, Tipo=? WHERE ID_Pessoa=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getCpf());
            stmt.setString(2, pessoa.getUsuario());
            stmt.setString(3, pessoa.getEmail());
            stmt.setString(4, pessoa.getSenha());
            stmt.setInt(5, pessoa.getTipo());
            stmt.setInt(6, pessoa.getID_Pessoa());
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public void excluirPessoa(int idPessoa) {
        String sql = "DELETE FROM Pessoa WHERE ID_Pessoa=?";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            stmt.executeUpdate();
            ConexaoBancoDados.commit(); // Realiza o commit da transação
        } catch (SQLException e) {
            ConexaoBancoDados.rollback(); // Em caso de erro, faz o rollback
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    }

    public Pessoa obterPessoaPorId(int idPessoa) {
        String sql = "SELECT * FROM Pessoa WHERE ID_Pessoa=?";
        Pessoa pessoa = null;

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                String cpf = resultado.getString("cpf");
                String usuario = resultado.getString("Usuario");
                String email = resultado.getString("Email");
                String senha = resultado.getString("Senha");
                int tipo = resultado.getInt("Tipo");
                pessoa = new Pessoa(idPessoa, cpf, usuario, email, senha, tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }

        return pessoa;
    }

    public List<Pessoa> listarTodasPessoas() {
        List<Pessoa> listaPessoas = new ArrayList<>();
        String sql = "SELECT * FROM Pessoa";

        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int idPessoa = resultado.getInt("ID_Pessoa");
                String cpf = resultado.getString("cpf");
                String usuario = resultado.getString("Usuario");
                String email = resultado.getString("Email");
                String senha = resultado.getString("Senha");
                int tipo = resultado.getInt("Tipo");
                Pessoa pessoa = new Pessoa(idPessoa, cpf, usuario, email, senha, tipo);
                listaPessoas.add(pessoa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }

        return listaPessoas;
    }

    public Pessoa obterPessoaPorUsuarioESenha(String usuario, String senha) {
        String sql = "SELECT * FROM Pessoa WHERE Usuario=? AND Senha=?";
        Pessoa pessoa = null;
    
        try (Connection conexao = ConexaoBancoDados.abrirConexao();
                PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet resultado = stmt.executeQuery();
    
            if (resultado.next()) {
                int idPessoa = resultado.getInt("ID_Pessoa");
                String cpf = resultado.getString("cpf");
                String email = resultado.getString("Email");
                int tipo = resultado.getInt("Tipo");
                pessoa = new Pessoa(idPessoa, cpf, usuario, email, senha, tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBancoDados.fecharConexao(); // Fecha a conexão
        }
    
        return pessoa;
    }
    
}

