package io.github.filipolszewski.communication.message;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.ResponseHandler;
import io.github.filipolszewski.communication.login.LoginPayload;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.view.screens.HomeScreen;
import lombok.extern.java.Log;

@Log
public class MessageResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(response.success()) {
            // Get login payload
            MessagePayload payload = (MessagePayload) response.payload();
            String sender;

            if(payload.isSystemMessage()) {
                window.getChatScreen().appendSystemMessage(payload.message());
            }
            else {
                sender = client.getUser().getUserID().equals(payload.senderID())
                        ? "You"
                        : payload.senderID();

                window.getChatScreen().appendUserMessage(payload.message(), sender);
            }

            window.getChatScreen().clearInput();
        }
        else {
            window.displayErrorDialog(response.message());
        }
    }
}
