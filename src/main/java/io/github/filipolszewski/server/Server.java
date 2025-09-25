package io.github.filipolszewski.server;

import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.MessagePayload;
import io.github.filipolszewski.constants.AppConfig;
import io.github.filipolszewski.server.services.RoomNotifierService;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.server.services.UserService;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Log
public class Server {

    private final UserService userService;
    private final RoomService roomService;
    private final RoomNotifierService roomNotifierService;
    private final Map<String, ClientHandler> clients;

    public Server() {
        clients = new ConcurrentHashMap<>();
        userService = new UserService();
        roomService = new RoomService();
        roomNotifierService = new RoomNotifierService(this, roomService);
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

                var handler = new ClientHandler(clientSocket, userService, roomService, roomNotifierService, this);
                clientPool.submit(handler);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeClientHandler(String userID) {
        clients.remove(userID);
    }

    public void addClientHandler(String userID, ClientHandler clientHandler) {
        clients.putIfAbsent(userID, clientHandler);
    }

    public ClientHandler getClientHandler(String userID) {
        return clients.get(userID);
    }


    /**
     * Broadcasts a generic response to all the clients connected to the server
     * @param response      Response sent back to the clients
     * @throws IOException  IOException If an I/O error occurs during the write operation
     */
    public void broadcastAll(Response<?> response) throws IOException {
        for(ClientHandler client : clients.values()) {
            client.getConn().send(response);
        }
    }

    /**
     * User version of the broadcast method (Takes in the sender)
     * Sends back a response with message payload (broadcasts) to all the clients but the sender
     * @param roomID        ID of the room to broadcast the message in
     * @param senderID      ID of person sending the message
     * @param message       The message itself
     * @throws IOException  IOException If an I/O error occurs during the write operation
     */
    public void broadcastMessageRoom(String roomID, String senderID, String message) throws IOException {
        for(String userID : roomService.getRoom(roomID).getActiveUsers()) {
            if(userID.equals(senderID)) continue;
            var client = clients.get(userID);
            client.getConn().send(new Response<>(null, new MessagePayload(message, senderID)));
        }
    }

    /**
     * System version of the broadcast method (No sender, sent by system)
     * Sends back a response with message payload (broadcasts) to all the clients
     * @param roomID        ID of the room to broadcast the message in
     * @param message       The message itself
     * @throws IOException  IOException If an I/O error occurs during the write operation
     */
    public void broadcastMessageRoom(String roomID, String message) throws IOException {
        for(String userID : roomService.getRoom(roomID).getActiveUsers()) {
            var client = clients.get(userID);
            client.getConn().send(new Response<>(null, new MessagePayload(message, true)));
        }
    }
}
