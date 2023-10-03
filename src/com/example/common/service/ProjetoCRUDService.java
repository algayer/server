package com.example.common.service;

import com.example.common.dao.ProjetoDAO;
import com.example.common.model.Projeto;
import java.util.List;

public class ProjetoCRUDService implements CRUDService<Projeto> {
    private ProjetoDAO projetoDAO;

    public ProjetoCRUDService() {
        projetoDAO = new ProjetoDAO();
    }

    @Override
    public void criar(Projeto projeto) {
        projetoDAO.inserirProjeto(projeto);
    }

    @Override
    public Projeto ler(int id) {
        return projetoDAO.obterProjetoPorId(id);
    }

    @Override
    public void atualizar(Projeto projeto) {
        projetoDAO.atualizarProjeto(projeto);
    }

    @Override
    public void excluir(int id) {
        projetoDAO.excluirProjeto(id);
    }

    @Override
    public List<Projeto> listarTodos() {
        return projetoDAO.listarTodosProjetos();
    }
}

