package io.github.filipolszewski.server;

import io.github.filipolszewski.constants.AppConfig;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

@Log
public class Server {

    /**
     * Create a new server socket and wait for connection from the client side
     * Create a new ClientHandler class for each client connection
     * Create a new Virtual Thread pool and submit a new ClientHandler
     */
    public void init() {
        try (var server = new ServerSocket(AppConfig.PORT);
             var clientPool = Executors.newVirtualThreadPerTaskExecutor()) {

            while(!server.isClosed()) {
                log.info("Server listening on port " + AppConfig.PORT + "...");

                Socket client = server.accept();

                log.info("A new client (" + client.getRemoteSocketAddress()
                        + ") has successfully been connected.");

                var handler = new ClientHandler(client);
                clientPool.submit(handler);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
