package com.example.desktopclient;

import com.example.common.utils.RequestObject;
import com.example.common.utils.ResponseObject;
import com.example.common.model.Pessoa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain {

    private final String SERVER_ADDRESS = "10.0.2.2";
    private final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        ClientMain client = new ClientMain();
        
        // Exemplo de chamada
        ResponseObject response = client.sendLoginRequest("testUser", "testPassword");
        System.out.println(response.getMessage());
    }

    public ResponseObject sendRequest(RequestObject request) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Sending request to server...");
            out.writeObject(request);
            out.flush();

            Object response = in.readObject();
            if (response instanceof ResponseObject) {
                return (ResponseObject) response;
            } else {
                return new ResponseObject(false, "Invalid response from server", null);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ResponseObject(false, "Error: " + e.getMessage(), null);
        }
    }

    public ResponseObject sendLoginRequest(String username, String password) {
        System.out.println("Sending login request for user: " + username);
        
        // Cria um objeto Pessoa para enviar como dado da requisição
        Pessoa user = new Pessoa(username, password);
        
        RequestObject request = new RequestObject("login", user);
        return sendRequest(request);
    }
}
