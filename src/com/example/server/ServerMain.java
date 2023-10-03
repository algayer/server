package com.example.server;

import com.example.common.model.Anexo;
import com.example.common.model.Equipe;
import com.example.common.model.Pessoa;
import com.example.common.model.Projeto;
import com.example.common.model.Tarefa;
import com.example.common.service.AnexoCRUDService;
import com.example.common.service.EquipeCRUDService;
import com.example.common.service.PessoaCRUDService;
import com.example.common.service.ProjetoCRUDService;
import com.example.common.service.TarefaCRUDService;
import com.example.common.utils.RequestObject;
import com.example.common.utils.ResponseObject;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static final int PORT = 8080;

    private static final PessoaCRUDService pessoaService = new PessoaCRUDService();
    private static final EquipeCRUDService equipeService = new EquipeCRUDService();
    private static final ProjetoCRUDService projetoService = new ProjetoCRUDService();
    private static final TarefaCRUDService tarefaService = new TarefaCRUDService();
    private static final AnexoCRUDService anexoService = new AnexoCRUDService();
    private static final String TERMINATION_STRING = "TERMINATION_SIGNAL";

    public static void main(String[] args) {
        try ( ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
    
            System.out.println("Cliente conectado: " + clientSocket.getRemoteSocketAddress());
    
            while (true) {
                RequestObject request;
                try {
                    request = (RequestObject) in.readObject();
                    System.out.println("Requisição recebida: " + request.getOperation() + ", Dados: " + request.getData());
                } catch (EOFException e) {
                    System.out.println("Conexão finalizada pelo cliente");
                    return;
                }
    
                ResponseObject response = processRequest(request);
                System.out.println("Enviando resposta: Sucesso=" + response.isSuccess() + ", Mensagem: " + response.getMessage() + ", Dados: " + response.getData());
                
                out.writeObject(response);
                out.flush();
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static ResponseObject processRequest(RequestObject request) {
        try {
            switch (request.getOperation()) {
                // Pessoa operations
                case "criarPessoa":
                    pessoaService.criar((Pessoa) request.getData());
                    return new ResponseObject(true, "Pessoa criada com sucesso", null);
                case "lerPessoa":
                    return new ResponseObject(true, "", pessoaService.ler((int) request.getData()));
                case "atualizarPessoa":
                    pessoaService.atualizar((Pessoa) request.getData());
                    return new ResponseObject(true, "Pessoa atualizada com sucesso", null);
                case "excluirPessoa":
                    pessoaService.excluir((int) request.getData());
                    return new ResponseObject(true, "Pessoa excluída com sucesso", null);
                case "listarTodosPessoa":
                    return new ResponseObject(true, "", pessoaService.listarTodos());
                case "login":
                    return pessoaService.login((Pessoa) request.getData());

                // Operações para Equipe
                case "criarEquipe":
                    equipeService.criar((Equipe) request.getData());
                    return new ResponseObject(true, "Equipe criada com sucesso", null);
                case "lerEquipe":
                    return new ResponseObject(true, "", equipeService.ler((int) request.getData()));
                case "atualizarEquipe":
                    equipeService.atualizar((Equipe) request.getData());
                    return new ResponseObject(true, "Equipe atualizada com sucesso", null);
                case "excluirEquipe":
                    equipeService.excluir((int) request.getData());
                    return new ResponseObject(true, "Equipe excluída com sucesso", null);
                case "listarTodosEquipe":
                    return new ResponseObject(true, "", equipeService.listarTodos());

                // Operações para Projeto
                case "criarProjeto":
                    projetoService.criar((Projeto) request.getData());
                    return new ResponseObject(true, "Projeto criado com sucesso", null);
                case "lerProjeto":
                    return new ResponseObject(true, "", projetoService.ler((int) request.getData()));
                case "atualizarProjeto":
                    projetoService.atualizar((Projeto) request.getData());
                    return new ResponseObject(true, "Projeto atualizado com sucesso", null);
                case "excluirProjeto":
                    projetoService.excluir((int) request.getData());
                    return new ResponseObject(true, "Projeto excluído com sucesso", null);
                case "listarTodosProjeto":
                    return new ResponseObject(true, "", projetoService.listarTodos());

                // Operações para Tarefa
                case "criarTarefa":
                    tarefaService.criar((Tarefa) request.getData());
                    return new ResponseObject(true, "Tarefa criada com sucesso", null);
                case "lerTarefa":
                    return new ResponseObject(true, "", tarefaService.ler((int) request.getData()));
                case "atualizarTarefa":
                    tarefaService.atualizar((Tarefa) request.getData());
                    return new ResponseObject(true, "Tarefa atualizada com sucesso", null);
                case "excluirTarefa":
                    tarefaService.excluir((int) request.getData());
                    return new ResponseObject(true, "Tarefa excluída com sucesso", null);
                case "listarTodosTarefa":
                    return new ResponseObject(true, "", tarefaService.listarTodos());

                // Operações para Anexo
                case "criarAnexo":
                    anexoService.criar((Anexo) request.getData());
                    return new ResponseObject(true, "Anexo criado com sucesso", null);
                case "lerAnexo":
                    return new ResponseObject(true, "", anexoService.ler((int) request.getData()));
                case "atualizarAnexo":
                    anexoService.atualizar((Anexo) request.getData());
                    return new ResponseObject(true, "Anexo atualizado com sucesso", null);
                case "excluirAnexo":
                    anexoService.excluir((int) request.getData());
                    return new ResponseObject(true, "Anexo excluído com sucesso", null);
                case "listarTodosAnexo":
                    return new ResponseObject(true, "", anexoService.listarTodos());

                default:
                    return new ResponseObject(false, "Operação não suportada", null);
            }
        } catch (Exception e) {
            return new ResponseObject(false, "Erro ao processar a requisição: " + e.getMessage(), null);
        }
    }
}
