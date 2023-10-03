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
            return new ResponseObject(true, "Login bem-sucedido", pessoaEncontrada);
        } else {
            return new ResponseObject(false, "Usuário ou senha inválidos", null);
        }
    }

}

