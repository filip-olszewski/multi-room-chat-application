package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import javax.swing.*;

public class FetchRoomsResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        // Get the payload
        FetchRoomsPayload payload = (FetchRoomsPayload) response.payload();

        // If privacy is public, add to the list
        if(payload.privacy() == RoomPrivacyPolicy.PUBLIC) {

            // Clear the public room list
            client.getPublicRooms().clear();

            // Put rooms in the public rooms map
            payload.rooms().forEach(room -> {
                client.getPublicRooms().put(room.getRoomID(), room);
            });

            // Update UI in swing's thread
            SwingUtilities.invokeLater(client::refreshRoomsListUI);
        }
    }
}
