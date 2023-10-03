package com.example.desktopclient;

import com.example.common.utils.RequestObject;
import com.example.common.utils.ResponseObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientService {

    private final String SERVER_ADDRESS = "10.0.2.2";
    private final int SERVER_PORT = 8080;

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

    public ResponseObject sendLoginRequest(String username, String password) {
        RequestObject request = new RequestObject("login", username + ":" + password);
        return sendRequest(request);
    }
}
