package io.github.filipolszewski.communication.createroom;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.view.screens.ChatScreen;

import javax.swing.*;

public class CreateRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(response.success()) {
            // Get payload
            CreateRoomPayload payload = (CreateRoomPayload) response.payload();

            // Display dialog and change the screen
            window.displaySuccessDialog(response.message());
            window.showScreen(ChatScreen.CHAT_SCREEN_KEY);

            // Add room to UI
            SwingUtilities.invokeLater(() -> {
                client.addRoomListing(payload.roomID());
            });
        }
        else {
            window.displayErrorDialog(response.message());
        }
    }
}
