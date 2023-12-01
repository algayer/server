package com.example.common.service;

import com.example.common.dao.TarefaDAO;
import com.example.common.model.Tarefa;
import com.example.common.utils.ResponseObject;
import java.util.List;

public class TarefaCRUDService implements CRUDService<Tarefa> {

    private TarefaDAO tarefaDAO;

    public TarefaCRUDService() {
        tarefaDAO = new TarefaDAO();
    }

    @Override
    public void criar(Tarefa tarefa) {
        tarefaDAO.inserirTarefa(tarefa);
    }

    @Override
    public Tarefa ler(int id) {
        return tarefaDAO.obterTarefaPorId(id);
    }

    @Override
    public void atualizar(Tarefa tarefa) {
        tarefaDAO.atualizarTarefa(tarefa);
    }

    @Override
    public void excluir(int id) {
        tarefaDAO.excluirTarefa(id);
    }

    @Override
    public List<Tarefa> listarTodos() {
        return tarefaDAO.listarTodasTarefas();
    }

    public ResponseObject listarTarefasPorProjeto(int projectId) {
        List<Tarefa> tarefas = tarefaDAO.listarTarefasPorProjeto(projectId);
        if (!tarefas.isEmpty()) {
            return new ResponseObject(true, "Tarefas listadas com sucesso", tarefas);
        } else {
            return new ResponseObject(false, "Nenhuma tarefa encontrada para o projeto com ID: " + projectId, null);
        }
    }
}
