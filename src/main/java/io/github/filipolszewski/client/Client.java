package io.github.filipolszewski.client;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.ResponseHandler;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.communication.createroom.CreateRoomResponseHandler;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomPayload;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomResponseHandler;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.communication.joinroom.JoinRoomResponseHandler;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomPayload;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomResponseHandler;
import io.github.filipolszewski.communication.login.LoginPayload;
import io.github.filipolszewski.communication.login.LoginResponseHandler;
import io.github.filipolszewski.connection.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.AppConfig;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.uicommands.CreateRoomCommand;
import io.github.filipolszewski.uicommands.DeleteRoomCommand;
import io.github.filipolszewski.uicommands.JoinRoomCommand;
import io.github.filipolszewski.uicommands.LeaveRoomCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Log
public class Client {

    private Connection<Request<? extends Payload>, Response<? extends Payload>> conn;

    @Getter
    private final AppWindow window;

    private final Map<Class<? extends Payload>, ResponseHandler> responseHandlers;
    private User user;

    public Client() {
        window = new AppWindow();
        responseHandlers = new HashMap<>();
        responseHandlers.put(LoginPayload.class, new LoginResponseHandler());
        responseHandlers.put(CreateRoomPayload.class, new CreateRoomResponseHandler());
        responseHandlers.put(DeleteRoomPayload.class, new DeleteRoomResponseHandler());
        responseHandlers.put(LeaveRoomPayload.class, new LeaveRoomResponseHandler());
        responseHandlers.put(JoinRoomPayload.class, new JoinRoomResponseHandler());
    }

    public void init() {
        try {
            conn = new SocketConnection<>(new Socket(AppConfig.HOST, AppConfig.PORT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Client successfully connected to the server.");

        window.showWindow();

        // Register commands
        CreateRoomCommand createRoomCommand = new CreateRoomCommand(conn, window);
        window.getHomeScreen().addCreateButtonListener(e -> {
            createRoomCommand.execute();
        });

        DeleteRoomCommand deleteRoomCommand = new DeleteRoomCommand(conn, window);
        window.getHomeScreen().addDeleteButtonListener(e -> {
            deleteRoomCommand.execute();
        });

        JoinRoomCommand joinRoomCommand = new JoinRoomCommand(conn, window);
        window.getHomeScreen().addJoinButtonListener(e -> {
            joinRoomCommand.execute();
        });

        LeaveRoomCommand leaveRoomCommand = new LeaveRoomCommand(conn, window);
        window.getChatScreen().addLeaveButtonListener(e -> {
            leaveRoomCommand.execute();
        });

        // Try to log in
        requestLogin();

        // Handle incoming responses
        handleResponse();
    }

    private void handleResponse() {
        new Thread(() -> {
            while(true) {

                Response<? extends Payload> response = null;

                try {
                    response = conn.recieve();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                log.info("Received response from server");

                ResponseHandler handler = responseHandlers.get(response.payload().getClass());
                log.info("Handling response...");


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

    public void setLoggedInUser(User user) {
        this.user = user;
    }
}
