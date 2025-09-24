package io.github.filipolszewski.server;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.communication.createroom.CreateRoomRequestHandler;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomPayload;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomRequestHandler;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.communication.joinroom.JoinRoomRequestHandler;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomPayload;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomRequestHandler;
import io.github.filipolszewski.communication.login.LoginPayload;
import io.github.filipolszewski.communication.login.LoginRequestHandler;
import io.github.filipolszewski.connection.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.managers.RoomManager;
import io.github.filipolszewski.server.managers.UserManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@Log
public class ClientHandler implements Runnable {

    @Getter
    private final Connection<Response<? extends Payload>, Request<? extends Payload>> conn;

    private final Map<Class<? extends Payload>, RequestHandler> requestHandlers;

    private final Socket clientSocketRef;
    private final Server serverRef;
    @Getter private final RoomManager roomManagerRef;
    @Getter private final UserManager userManagerRef;

    @Getter @Setter
    private User user;

    public ClientHandler(Socket clientSocket, UserManager userManager, RoomManager roomManager, Server server) {
        requestHandlers = new HashMap<>();
        requestHandlers.put(LoginPayload.class, new LoginRequestHandler());
        requestHandlers.put(CreateRoomPayload.class, new CreateRoomRequestHandler());
        requestHandlers.put(DeleteRoomPayload.class, new DeleteRoomRequestHandler());
        requestHandlers.put(JoinRoomPayload.class, new JoinRoomRequestHandler());
        requestHandlers.put(LeaveRoomPayload.class, new LeaveRoomRequestHandler());

        this.userManagerRef = userManager;
        this.roomManagerRef = roomManager;
        this.serverRef = server;
        this.clientSocketRef = clientSocket;

        try {
            conn = new SocketConnection<>(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                // Receive request
                Request<?> request = null;
                request = conn.recieve();
                log.info("Received request from client: " + request);

                // Handle request
                RequestHandler handler = requestHandlers.get(request.payload().getClass());
                log.info("Handling request... " + request);

                // Check if there's a handler registered for this request type
                if(handler == null) {
                    log.info("Failed to handle request. Handler not registered for: " + request.getClass());
                    continue;
                }

                // Handle the request
                var response = handler.handle(request, this);

                // Log based on response success
                if(response.success())
                    log.info("Request has been handled successfully! Responding: " + response);
                else log.info("Failed to handle request successfully! Responding: " + response);

                // Send back the response
                conn.send(response);
            }

        }
        catch (SocketException e) {
            log.info("Client has disconnected.");
        }
        catch (IOException | ClassNotFoundException e) {
            log.severe("An unexpected I/O or deserialization error occurred: " + e.getMessage());
        }
        finally {
            serverRef.removeClientHandler(this);

            if (user != null) {
                log.info("Logging out user: " + user.getUserID());
                userManagerRef.removeUser(user.getUserID());
                roomManagerRef.leaveRoom(user.getCurrentRoomID(), user.getUserID());
            }
        }
    }
}
