package com.example.desktopclient;

import com.example.common.utils.RequestObject;
import com.example.common.utils.ResponseObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain {
    private final String SERVER_ADDRESS = "10.0.2.2";
    private final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        ClientMain client = new ClientMain();
        // Exemplo de como usar
        ResponseObject response = client.sendRequest(new RequestObject("login", "testUser:testPassword"));
        System.out.println(response.getMessage());
    }

    public ResponseObject sendRequest(RequestObject request) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

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
}
