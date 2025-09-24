package io.github.filipolszewski.uicommands;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.message.MessagePayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.view.AppWindow;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class MessageCommand implements Command {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;
    private final String userID;

    public MessageCommand(Connection<Request<? extends Payload>, Response<? extends Payload>> conn,
                          AppWindow window,
                          String userID) {
        this.conn = conn;
        this.window = window;
        this.userID = userID;
    }

    public void execute() {
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
