package io.github.filipolszewski.client;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.login.LoginPayload;
import io.github.filipolszewski.connection.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.AppConfig;
import io.github.filipolszewski.uicommands.CreateRoomCommand;
import io.github.filipolszewski.uicommands.DeleteRoomCommand;
import io.github.filipolszewski.uicommands.JoinRoomCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class Client {

    private Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    public Client() {
        window = new AppWindow();
    }

    /**
     * Initialize new client side connection
     * Handle first time login request
     */
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

        // Try to log in
        requestLogin();

        while(true) {
            try {
                Response<? extends Payload> response = conn.recieve();
                // new LoginResponseHandler().handle(response);

                log.info(response.toString());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void requestLogin() {
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

        Request<LoginPayload> request = new Request<>(new LoginPayload(username));

        try {
            conn.send(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
