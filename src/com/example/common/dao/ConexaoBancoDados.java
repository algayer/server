package com.example.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBancoDados {
    private static final String URL = "jdbc:mysql://localhost:3306/TrabalhoFinal";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private static Connection conexao;

    public static Connection abrirConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                conexao.setAutoCommit(false); // Desliga o autocommit
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC não encontrado.", e);
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                // conexao.setAutoCommit(true); // Reativar o autocommit ao finalizar a transação
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit() {
        try {
            if (conexao != null) {
                conexao.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {
        try {
            if (conexao != null) {
                conexao.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

