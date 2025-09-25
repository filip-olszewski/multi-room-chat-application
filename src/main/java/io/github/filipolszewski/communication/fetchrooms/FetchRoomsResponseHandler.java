package io.github.filipolszewski.communication.fetchrooms;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.ResponseHandler;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
