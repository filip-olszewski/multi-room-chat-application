package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.JoinRoomPayload;

public class JoinRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        final JoinRoomPayload payload = (JoinRoomPayload) response.payload();

        // Display success dialog and change the screen
        window.displaySuccessDialog(response.message());
        window.showChatScreen();

        // Set current room ID for user on the client side
        client.getUser().setCurrentRoomID(payload.roomID());
    }
}
