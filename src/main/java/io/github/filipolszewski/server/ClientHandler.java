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
import java.util.HashMap;
import java.util.Map;

@Log
public class ClientHandler implements Runnable {

    @Getter
    private final Connection<Response<? extends Payload>, Request<? extends Payload>> conn;

    private final Map<Class<? extends Payload>, RequestHandler> requestHandlers;

    private final Socket clientSocketRef;
    @Getter private final RoomManager roomManagerRef;
    @Getter private final UserManager userManagerRef;

    @Getter @Setter
    private User user;

    public ClientHandler(Socket clientSocket, UserManager userManager, RoomManager roomManager) {
        requestHandlers = new HashMap<>();
        requestHandlers.put(LoginPayload.class, new LoginRequestHandler());
        requestHandlers.put(CreateRoomPayload.class, new CreateRoomRequestHandler());
        requestHandlers.put(DeleteRoomPayload.class, new DeleteRoomRequestHandler());
        requestHandlers.put(JoinRoomPayload.class, new JoinRoomRequestHandler());
        requestHandlers.put(LeaveRoomPayload.class, new LeaveRoomRequestHandler());

        this.userManagerRef = userManager;
        this.roomManagerRef = roomManager;
        this.clientSocketRef = clientSocket;

        try {
            conn = new SocketConnection<>(clientSocket);
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

            // Receive request
            Request<?> request = null;

            try {
                request = conn.recieve();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            log.info("Received from client: " + request);


            // Handle request
            RequestHandler handler = requestHandlers.get(request.payload().getClass());

            log.info("Handling request... " + request);

            if(handler != null) {
                var response = handler.handle(request, this);

                if(response.success()) {
                    log.info("Request has been handled successfully! " + request);
                }
                else {
                    log.info("Failed to handle request successfully! " + request);
                }

                try {
                    conn.send(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                log.info("Failed to handle request. Handle not registered for: " + request.getClass());
            }
        }
    }
}
