package io.github.filipolszewski.client.commands.impl;

import io.github.filipolszewski.client.commands.NoArgsCommand;
import io.github.filipolszewski.client.ui.AppWindow;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.LoginPayload;
import io.github.filipolszewski.connection.Connection;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginCommand implements NoArgsCommand {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    @Override
    public void execute() {
        String username = window.promptInputDialog("Please input your username");
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
