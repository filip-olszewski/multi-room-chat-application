package io.github.filipolszewski.client.commands.impl;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.MessagePayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.client.commands.ParamCommand;
import io.github.filipolszewski.client.ui.AppWindow;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
@RequiredArgsConstructor
public class MessageCommand implements ParamCommand<String> {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    @Override
    public void execute(String userID) {
        String message = window.getChatScreen().getInputText();
        if(message == null || message.trim().isEmpty()) return;

        if(userID == null) {
            log.info("Cannot send the message as the user is not logged in!");
            return;
        }

        Request<MessagePayload> req = new Request<>(new MessagePayload(message, userID));

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
