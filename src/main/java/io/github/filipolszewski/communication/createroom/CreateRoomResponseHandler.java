package io.github.filipolszewski.communication.createroom;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.view.screens.ChatScreen;

public class CreateRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(response.success()) {
            CreateRoomPayload payload = (CreateRoomPayload) response.payload();
            window.displaySuccessDialog(response.message());
            window.showScreen(ChatScreen.CHAT_SCREEN_KEY);
        }
        else {
            window.displayErrorDialog(response.message());
        }
    }
}
