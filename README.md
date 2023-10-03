public ResponseObject sendRequest(RequestObject request) {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

        System.out.println("Sending request: " + request.getOperation());

        out.writeObject(request);
        out.flush();

        System.out.println("Waiting for response...");

        Object response = in.readObject();
        System.out.println("Response received: " + ((ResponseObject) response).getMessage());

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
