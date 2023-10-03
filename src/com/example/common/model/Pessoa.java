package com.example.common.model;

import java.io.Serializable;

public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ID_Pessoa;
    private String cpf;
    private String Usuario;
    private String Email;
    private String Senha;
    private int Tipo; // 0 para gerente, 1 para funcionário

    public Pessoa() {
        // Construtor vazio é necessário para serialização
    }

    public Pessoa(int ID_Pessoa, String cpf, String Usuario, String Email, String Senha, int Tipo) {
        this.ID_Pessoa = ID_Pessoa;
        this.cpf = cpf;
        this.Usuario = Usuario;
        this.Email = Email;
        this.Senha = Senha;
        this.Tipo = Tipo;
    }

    // Getters e Setters
    public int getID_Pessoa() {
        return ID_Pessoa;
    }

    public void setID_Pessoa(int ID_Pessoa) {
        this.ID_Pessoa = ID_Pessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "ID_Pessoa=" + ID_Pessoa +
                ", cpf='" + cpf + '\'' +
                ", Usuario='" + Usuario + '\'' +
                ", Email='" + Email + '\'' +
                ", Senha='" + Senha + '\'' +
                '}';
    }
}

