package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.dto.RoomDTO;

import javax.swing.*;
import java.util.Map;

public class FetchRoomsResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        // Get the payload
        final FetchRoomsPayload payload = (FetchRoomsPayload) response.payload();

        // For now fetch only public rooms as we only need to display these
        // in the home screen
        // In the future when the app scales, the behaviour may change

        // If privacy is public, add to the list
        if(payload.privacy() != RoomPrivacyPolicy.PUBLIC) {
            return;
        }


        Map<String, RoomDTO> publicRooms = client.getPublicRooms();

        // Update the room map & append room listing to UI
        payload.rooms().forEach(room -> {
            publicRooms.put(room.roomID(), room);
            client.addRoomListingToUI(room);
        });


        // Update UI in swing's thread
        SwingUtilities.invokeLater(client::refreshRoomsListUI);
    }
}
