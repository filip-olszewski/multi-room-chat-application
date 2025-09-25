package io.github.filipolszewski.server;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.CreateRoomPayload;
import io.github.filipolszewski.server.events.impl.RoomModifiedEvent;
import io.github.filipolszewski.server.handlers.CreateRoomRequestHandler;
import io.github.filipolszewski.communication.payloads.DeleteRoomPayload;
import io.github.filipolszewski.server.handlers.DeleteRoomRequestHandler;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.server.handlers.FetchRoomsRequestHandler;
import io.github.filipolszewski.communication.payloads.JoinRoomPayload;
import io.github.filipolszewski.server.handlers.JoinRoomRequestHandler;
import io.github.filipolszewski.communication.payloads.LeaveRoomPayload;
import io.github.filipolszewski.server.handlers.LeaveRoomRequestHandler;
import io.github.filipolszewski.communication.payloads.LoginPayload;
import io.github.filipolszewski.server.handlers.LoginRequestHandler;
import io.github.filipolszewski.communication.payloads.MessagePayload;
import io.github.filipolszewski.server.handlers.MessageRequestHandler;
import io.github.filipolszewski.connection.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.util.mappers.RoomMapper;
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

    private final Socket clientSocketRef;
    private final Map<Class<? extends Payload>, RequestHandler> requestHandlers;

    @Getter
    private final Server server;

    @Getter
    private final ServerContext context;

    @Getter @Setter
    private String userID;


    public ClientHandler(Socket clientSocket,
                         ServerContext context,
                         Server server) {

        requestHandlers = new HashMap<>();
        requestHandlers.put(LoginPayload.class, new LoginRequestHandler());
        requestHandlers.put(CreateRoomPayload.class, new CreateRoomRequestHandler());
        requestHandlers.put(DeleteRoomPayload.class, new DeleteRoomRequestHandler());
        requestHandlers.put(JoinRoomPayload.class, new JoinRoomRequestHandler());
        requestHandlers.put(LeaveRoomPayload.class, new LeaveRoomRequestHandler());
        requestHandlers.put(MessagePayload.class, new MessageRequestHandler());
        requestHandlers.put(FetchRoomsPayload.class, new FetchRoomsRequestHandler());

        this.context = context;
        this.server = server;
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
                log.info("Request has been handled successfully! Responding: " + response);

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
            server.removeClientHandler(userID);

            if (userID != null && context.userService().getUser(userID) != null) {
                User u = context.userService().getUser(userID);

                context.userService().removeUser(userID);
                context.roomService().leaveRoom(u.getCurrentRoomID(), userID);

                // Signal room update to other clients
                context.eventBus().emit(new RoomModifiedEvent(RoomMapper.toDTO(
                        context.roomService().getRoom(u.getCurrentRoomID())
                )));

                log.info("Logging out user: " + userID);
            }
        }
    }
}
