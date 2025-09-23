package io.github.filipolszewski.server;

import io.github.filipolszewski.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Connection conn;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            conn = new SocketConnection(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle reading incoming requests from the clients
     */
    @Override
    public void run() {
        while(true) {
            try {
                String packet = conn.recieve();
                log.info("Server reading from client: " + packet);

                String message = "Hello from server!";
                log.info("Server writing to client: " + message);
                conn.send(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
