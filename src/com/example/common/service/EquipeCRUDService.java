package com.example.common.service;
import com.example.common.dao.EquipeDAO;
import com.example.common.model.Equipe;
import java.util.List;


public class EquipeCRUDService implements CRUDService<Equipe> {
    private EquipeDAO equipeDAO;

    public EquipeCRUDService() {
        equipeDAO = new EquipeDAO();
    }

    @Override
    public void criar(Equipe equipe) {
        equipeDAO.inserirEquipe(equipe);
    }

    @Override
    public Equipe ler(int id) {
        return equipeDAO.obterEquipePorId(id);
    }

    @Override
    public void atualizar(Equipe equipe) {
        equipeDAO.atualizarEquipe(equipe);
    }

    @Override
    public void excluir(int id) {
        equipeDAO.excluirEquipe(id);
    }

    @Override
    public List<Equipe> listarTodos() {
        return equipeDAO.listarTodasEquipes();
    }
}

