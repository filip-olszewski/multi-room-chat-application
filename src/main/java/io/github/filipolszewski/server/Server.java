package io.github.filipolszewski.server;

import io.github.filipolszewski.constants.AppConfig;
import io.github.filipolszewski.server.managers.RoomManager;
import io.github.filipolszewski.server.managers.UserManager;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

@Log
public class Server {

    private final UserManager userManager;
    private final RoomManager roomManager;
    private final List<ClientHandler> clients;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        userManager = new UserManager();
        roomManager = new RoomManager();
    }

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

                Socket clientSocket = server.accept();

                log.info("A new client (" + clientSocket.getRemoteSocketAddress()
                        + ") has successfully been connected.");

                var handler = new ClientHandler(clientSocket, userManager, roomManager);
                clients.add(handler);
                clientPool.submit(handler);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
