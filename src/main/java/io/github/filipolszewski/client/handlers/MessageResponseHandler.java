package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.MessagePayload;
import lombok.extern.java.Log;

@Log
public class MessageResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
        }

        // Get login payload
        final MessagePayload payload = (MessagePayload) response.payload();
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
}
