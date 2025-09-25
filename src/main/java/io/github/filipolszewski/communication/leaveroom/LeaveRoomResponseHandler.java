package io.github.filipolszewski.communication.leaveroom;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;

public class LeaveRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        // Clear chat, display success and change the screen
        window.getChatScreen().clearChat();
        window.displaySuccessDialog(response.message());
        window.showHomeScreen();

        // Remove the room ID from the user on the client side
        client.getUser().setCurrentRoomID(null);
    }
}
