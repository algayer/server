package com.example.common.service;

import com.example.common.dao.AnexoDAO;
import com.example.common.model.Anexo;
import java.util.List;

public class AnexoCRUDService implements CRUDService<Anexo> {
    private AnexoDAO anexoDAO;

    public AnexoCRUDService() {
        anexoDAO = new AnexoDAO();
    }

    @Override
    public void criar(Anexo anexo) {
        anexoDAO.inserirAnexo(anexo);
    }

    @Override
    public Anexo ler(int id) {
        return anexoDAO.obterAnexoPorId(id);
    }

    @Override
    public void atualizar(Anexo anexo) {
        anexoDAO.atualizarAnexo(anexo);
    }

    @Override
    public void excluir(int id) {
        anexoDAO.excluirAnexo(id);
    }

    @Override
    public List<Anexo> listarTodos() {
        return anexoDAO.listarTodosAnexos();
    }
}

