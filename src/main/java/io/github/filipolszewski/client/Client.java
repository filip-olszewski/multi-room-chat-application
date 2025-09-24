package io.github.filipolszewski.client;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.ResponseHandler;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.communication.createroom.CreateRoomResponseHandler;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomPayload;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomResponseHandler;
import io.github.filipolszewski.communication.fetchrooms.FetchRoomsPayload;
import io.github.filipolszewski.communication.fetchrooms.FetchRoomsResponseHandler;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.communication.joinroom.JoinRoomResponseHandler;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomPayload;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomResponseHandler;
import io.github.filipolszewski.communication.login.LoginPayload;
import io.github.filipolszewski.communication.login.LoginResponseHandler;
import io.github.filipolszewski.communication.message.MessagePayload;
import io.github.filipolszewski.communication.message.MessageResponseHandler;
import io.github.filipolszewski.connection.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.AppConfig;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.uicommands.*;
import io.github.filipolszewski.uicommands.impl.*;
import io.github.filipolszewski.view.AppWindow;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
public class Client {

    private final Map<Class<? extends Payload>, ResponseHandler> responseHandlers;
    @Getter private Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    @Getter private final AppWindow window;
    @Getter private final CommandRegistry commandRegistry;

    @Getter @Setter
    private User user;

    @Getter
    private Map<RoomPrivacyPolicy, List<Room>> rooms;

    public Client() {
        window = new AppWindow();
        responseHandlers = new HashMap<>();
        rooms = new HashMap<>();
        commandRegistry = new CommandRegistry();

        responseHandlers.put(LoginPayload.class, new LoginResponseHandler());
        responseHandlers.put(CreateRoomPayload.class, new CreateRoomResponseHandler());
        responseHandlers.put(DeleteRoomPayload.class, new DeleteRoomResponseHandler());
        responseHandlers.put(LeaveRoomPayload.class, new LeaveRoomResponseHandler());
        responseHandlers.put(JoinRoomPayload.class, new JoinRoomResponseHandler());
        responseHandlers.put(MessagePayload.class, new MessageResponseHandler());
        responseHandlers.put(FetchRoomsPayload.class, new FetchRoomsResponseHandler());
    }

    public void init() {
        try {
            conn = new SocketConnection<>(new Socket(AppConfig.HOST, AppConfig.PORT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Client successfully connected to the server.");

        window.showWindow();

        // Register all commands
        registerCommands();

        // Add action listener to trigger commands
        bindUIActions();

        // Try to log in
        requestLogin();

        // Handle incoming responses
        handleResponse();
    }

    private void handleResponse() {
        new Thread(() -> {
            while(true) {

                // Try to read incoming response
                Response<? extends Payload> response = null;

                try {
                    response = conn.recieve();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                log.info("Received response from server");

                // Get a handler to handle the response
                ResponseHandler handler = responseHandlers.get(response.payload().getClass());

                log.info("Handling response...");

                // Handle the response
                if(handler != null) {
                    handler.handle(response, this);
                    log.info("Response (" + response + ") has been handled");
                }
                else {
                    log.info("Failed to handle request. Handle not registered for: "
                            + response.getClass());
                }
            }
        }).start();
    }

    public void requestLogin() {
        String username = window.promptInputDialog("Please input your username");

        // Exit if canceled
        if(username == null) System.exit(0);

        // Repeat the prompt if empty
        while(username.isEmpty()) {
            window.displayErrorDialog("You need to specify your username to connect");
            username = window.promptInputDialog("Please input your username");

            // Check again if canceled
            if(username == null) System.exit(0);
        }

        // Send login request
        Request<LoginPayload> request = new Request<>(new LoginPayload(username));

        try {
            conn.send(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Register all the commands to the registry
     */
    private void registerCommands() {
        commandRegistry.register(CreateRoomCommand.class, new CreateRoomCommand(conn, window));
        commandRegistry.register(DeleteRoomCommand.class, new DeleteRoomCommand(conn, window));
        commandRegistry.register(JoinRoomCommand.class, new JoinRoomCommand(conn, window));
        commandRegistry.register(LeaveRoomCommand.class, new LeaveRoomCommand(conn, window));
        commandRegistry.register(MessageCommand.class, new MessageCommand(conn, window));
        commandRegistry.register(FetchRoomsCommand.class, new FetchRoomsCommand(conn, window));
    }

    private void bindUIActions() {
        // Register add button with create room command
        window.getHomeScreen().addCreateButtonListener(e -> {
           commandRegistry.getNoArgs(CreateRoomCommand.class).execute();
        });

        // Register delete button with delete room command
        window.getHomeScreen().addDeleteButtonListener(e -> {
            commandRegistry.getNoArgs(DeleteRoomCommand.class).execute();
        });

        // Register join button with join room command
        window.getHomeScreen().addJoinButtonListener(e -> {
            commandRegistry.getNoArgs(JoinRoomCommand.class).execute();
        });

        // Register leave button with leave room command
        window.getChatScreen().addLeaveButtonListener(e -> {
            commandRegistry.getNoArgs(LeaveRoomCommand.class).execute();
        });

        // Register refresh button with fetch rooms command
        window.getHomeScreen().addRefreshButtonListener(e -> {
            commandRegistry.getParam(FetchRoomsCommand.class).execute(RoomPrivacyPolicy.PUBLIC);
        });

        // Register send button with message command
        window.getChatScreen().addSendButtonActionListener(e -> {
            commandRegistry.getParam(MessageCommand.class).execute(user.getUserID());
        });
    }

    public void addRoomListing(String roomID) {
        window.getHomeScreen().addRoomListing(roomID, e -> {
            commandRegistry.getParam(JoinRoomCommand.class).execute(roomID);
        });
    }

    public void refreshRoomsList() {
        window.getHomeScreen().clearRoomListings();
        rooms.get(RoomPrivacyPolicy.PUBLIC).forEach(r -> addRoomListing(r.getRoomID()));
    }
}
