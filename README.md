package com.example.desktopclient;

import com.example.common.utils.RequestObject;
import com.example.common.utils.ResponseObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientService {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isConnected = false;

    private final static String SERVER_ADDRESS = "localhost";
    private final static int SERVER_PORT = 8080;

    public ClientService() {
        connect();
    }

    private void connect() {
        if (!isConnected) {
            try {
                System.out.println("Tentando conectar...");
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Conectado com sucesso.");
                isConnected = true;
            } catch (IOException e) {
                System.out.println("Erro ao conectar: " + e.getMessage());
                isConnected = false;
            }
        }
    }

    public ResponseObject sendLoginRequest(String username, String password) {
        RequestObject request = new RequestObject("login", username + ":" + password);
        return sendRequest(request);
    }

    // Você pode adicionar outros métodos públicos para enviar diferentes tipos de solicitações

    private ResponseObject sendRequest(RequestObject request) {
        try {
            System.out.println("Enviando requisição...");
            out.writeObject(request);
            out.flush();
            System.out.println("Requisição enviada.");
            return (ResponseObject) in.readObject();
        } catch (EOFException e) {
            System.out.println("Conexão perdida com o servidor");
            isConnected = false;
            return new ResponseObject(false, "Conexão perdida com o servidor", null);
        } catch (Exception e) {
            System.out.println("Erro ao enviar requisição: " + e.getMessage());
            e.printStackTrace();
            isConnected = false;
            return new ResponseObject(false, "Erro ao enviar requisição: " + e.getMessage(), null);
        }
    }

    public void closeConnection() {
        try {
            System.out.println("Fechando conexão...");
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            isConnected = false;
            System.out.println("Conexão fechada.");
        } catch (IOException e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
