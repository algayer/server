package controller;

import com.example.common.utils.RequestObject;
import com.example.common.utils.ResponseObject;
import com.example.common.model.Pessoa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexaoController {

    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int SERVER_PORT = 8080;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isConnected = false;
    private static final String TAG = "ConexaoController";

    public ConexaoController() {
        this.isConnected = connect();
    }

    private boolean connect() {
        try {
            System.out.println(TAG + ": Tentando conectar...");
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println(TAG + ": Conectado com sucesso.");
            isConnected = true;
        } catch (IOException e) {
            System.err.println(TAG + ": Erro ao conectar. " + e.getMessage());
            isConnected = false;
        }

        return isConnected;
    }

    public ResponseObject sendRequest(RequestObject request) {
        if (!isConnected) {
            System.out.println(TAG + ": Não está conectado. Tentando reconectar...");
            isConnected = connect();
            if (!isConnected) {
                System.err.println(TAG + ": Falha ao reconectar.");
                return new ResponseObject(false, "Falha ao reconectar.", null);
            }
        }

        try {
            System.out.println(TAG + ": Enviando requisição...");
            out.writeObject(request);
            out.flush();
            System.out.println(TAG + ": Requisição enviada. Aguardando resposta...");

            Object response = in.readObject();
            if (response instanceof ResponseObject) {
                return (ResponseObject) response;
            } else {
                System.err.println(TAG + ": Resposta inválida do servidor.");
                return new ResponseObject(false, "Resposta inválida do servidor.", null);
            }
        } catch (Exception e) {
            System.err.println(TAG + ": Erro ao enviar requisição ou ao receber resposta: " + e.getMessage());
            isConnected = false;
            return new ResponseObject(false, "Erro ao enviar requisição ou ao receber resposta: " + e.getMessage(), null);
        }
    }

    public ResponseObject sendLoginRequest(String username, String password) {
        System.out.println(TAG + ": Enviando requisição de login para o usuário: " + username);
        Pessoa user = new Pessoa(username, password);
        RequestObject request = new RequestObject("login", user);
        return sendRequest(request);
    }

    public void closeConnection() {
        try {
            System.out.println(TAG + ": Fechando conexão...");
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            isConnected = false;
            System.out.println(TAG + ": Conexão fechada.");
        } catch (IOException e) {
            System.err.println(TAG + ": Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
