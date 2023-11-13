package com.example.common.service;
import com.example.common.dao.EquipeDAO;
import com.example.common.model.Equipe;
import com.example.common.model.Pessoa;
import com.example.common.utils.ResponseObject;
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
    
    public List<Pessoa> listarMembros(int id){
        return equipeDAO.listarMembrosDaEquipe(id);
    }
    
    public ResponseObject buscarEquipePorUsuario(int idPessoa) {
        List<Equipe> equipes = equipeDAO.obterEquipePorPessoaId(idPessoa);
        if (equipes != null && !equipes.isEmpty()) {
            // Se a pessoa faz parte de mais de uma equipe, você pode decidir como lidar com isso.
            // Aqui estou retornando apenas a primeira equipe encontrada.
            return new ResponseObject(true, "Equipe encontrada com sucesso", equipes.get(0));
        } else {
            return new ResponseObject(false, "Nenhuma equipe encontrada para o usuário", null);
        }
    }
}

