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
