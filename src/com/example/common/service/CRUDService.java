package com.example.common.service;

import java.util.List;

public interface CRUDService<T> {
    void criar(T entidade);
    T ler(int id);
    void atualizar(T entidade);
    void excluir(int id);
    List<T> listarTodos();
}

