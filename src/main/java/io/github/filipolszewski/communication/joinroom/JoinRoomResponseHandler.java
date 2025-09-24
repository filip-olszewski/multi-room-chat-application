package io.github.filipolszewski.communication.joinroom;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.view.screens.ChatScreen;

public class JoinRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(response.success()) {
            JoinRoomPayload payload = (JoinRoomPayload) response.payload();

            window.displaySuccessDialog(response.message());
            window.showScreen(ChatScreen.CHAT_SCREEN_KEY);

            client.getUser().setCurrentRoomID(payload.roomID());
        }
        else {
            window.displayErrorDialog(response.message());
        }
    }
}
