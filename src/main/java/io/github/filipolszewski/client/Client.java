package io.github.filipolszewski.client;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.communication.login.LoginPayload;
import io.github.filipolszewski.communication.login.LoginResponseHandler;
import io.github.filipolszewski.connection.SocketConnection;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.AppConfig;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;

@Log
public class Client {

    private Connection<Request<? extends Payload>, Response<? extends Payload>> conn;

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


        Request<LoginPayload> request = new Request<>(new LoginPayload("mark"));

        try {
            conn.send(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while(true) {
            try {
                Response<? extends Payload> response = conn.recieve();
                new LoginResponseHandler().handle(response);

                log.info(response.toString());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
