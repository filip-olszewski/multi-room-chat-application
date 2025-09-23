package io.github.filipolszewski.client;

import io.github.filipolszewski.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.AppConfig;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class Client {

    private Connection conn;

    /**
     * Initialize new client side connection
     * Handle first time login request
     */
    public void init() {
        try {
            conn = new SocketConnection(new Socket(AppConfig.HOST, AppConfig.PORT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Client successfully connected to the server.");

        // Request login
        String message = "Hello from client!";
        log.info("Client writing to server: " + message);
        conn.send(message);

        while(true) {
            try {
                String packet = conn.recieve();
                log.info("Client reading from server: " + packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
