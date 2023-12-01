package com.example.common.service;

import com.example.common.dao.PessoaDAO;
import com.example.common.model.Pessoa;
import com.example.common.utils.ResponseObject;
import java.util.List;

public class PessoaCRUDService implements CRUDService<Pessoa> {

    private PessoaDAO pessoaDAO;

    public PessoaCRUDService() {
        pessoaDAO = new PessoaDAO();
    }

    @Override
    public void criar(Pessoa pessoa) {
        pessoaDAO.inserirPessoa(pessoa);
    }

    @Override
    public Pessoa ler(int id) {
        return pessoaDAO.obterPessoaPorId(id);
    }

    @Override
    public void atualizar(Pessoa pessoa) {
        pessoaDAO.atualizarPessoa(pessoa);
    }

    @Override
    public void excluir(int id) {
        pessoaDAO.excluirPessoa(id);
    }

    @Override
    public List<Pessoa> listarTodos() {
        return pessoaDAO.listarTodasPessoas();
    }

    public ResponseObject login(Pessoa pessoa) {
        Pessoa pessoaEncontrada = pessoaDAO.obterPessoaPorUsuarioESenha(pessoa.getUsuario(), pessoa.getSenha());
        if (pessoaEncontrada != null) {
            System.out.println(pessoaEncontrada.toString());
            return new ResponseObject(true, "Login bem-sucedido", pessoaEncontrada);
        } else {
            return new ResponseObject(false, "Usuário ou senha inválidos", null);
        }
    }

    public Pessoa validarEmail(String email) {
        System.out.println("Validando email: " + email);
        Pessoa pessoa = pessoaDAO.obterPessoaPorEmail(email);

        if (pessoa != null) {
            System.out.println("Email válido");
            return pessoa;
        } else {
            System.out.println("Email inválido");
            return null;
        }
    }

    public void atualizarSenha(Pessoa pessoa) {
        pessoaDAO.atualizarSenha(pessoa);
    }

    public Pessoa validarSenha(String senha) {
        System.out.println("Validando senha: " + senha);
        Pessoa pessoa = pessoaDAO.obterPessoaPorSenha(senha);

        if (pessoa != null) {
            System.out.println("senha válido");
            return pessoa;
        } else {
            System.out.println("senha inválido");
            return null;
        }
    }

    public ResponseObject trocarSenha(String email, String senhaAntiga, String novaSenha) {
        Pessoa pessoa = pessoaDAO.obterPessoaPorEmail(email);
        if (pessoa != null && pessoa.getSenha().equals(senhaAntiga)) {
            pessoa.setSenha(novaSenha);
            pessoaDAO.atualizarSenha(pessoa);
            return new ResponseObject(true, "Senha atualizada com sucesso", null);
        } else {
            return new ResponseObject(false, "Senha antiga não corresponde", null);
        }
    }

}
