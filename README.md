private static void handleClient(Socket clientSocket) {
        try ( ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());  ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) { // Troque a ordem aqui

            System.out.println("Cliente conectado: " + clientSocket.getRemoteSocketAddress());

            while (true) {
                RequestObject request;
                try {
                    request = (RequestObject) in.readObject();
                } catch (EOFException e) {
                    System.out.println("Conexão finalizada pelo cliente");
                    return; // Finalize o loop se o cliente encerrar a conexão
                }

                System.out.println("Requisição recebida: " + request.getOperation());

                ResponseObject response = processRequest(request);
                out.writeObject(response);
                out.flush(); // Assegure-se de que os dados são enviados
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

------------------

public ResponseObject sendRequest(RequestObject request) {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) { // Mantenha essa ordem

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
