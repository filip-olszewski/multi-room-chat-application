package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;

public class LeaveRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        // Clear chat, change the screen and display success
        window.getChatScreen().clearChat();
        window.showHomeScreen();
        window.displaySuccessDialog(response.message());

        // Remove the room ID from the user on the client side
        client.getUser().setCurrentRoomID(null);
    }
}
