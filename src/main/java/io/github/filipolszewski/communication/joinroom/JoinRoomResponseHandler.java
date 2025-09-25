package io.github.filipolszewski.communication.joinroom;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;

public class JoinRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        JoinRoomPayload payload = (JoinRoomPayload) response.payload();

        // Display success dialog and change the screen
        window.displaySuccessDialog(response.message());
        window.showChatScreen();

        // Set current room ID for user on the client side
        client.getUser().setCurrentRoomID(payload.roomID());
    }
}
