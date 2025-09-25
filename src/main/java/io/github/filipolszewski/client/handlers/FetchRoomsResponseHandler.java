package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.RoomUpdate;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import javax.swing.*;

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

        // If privacy is public, add to the list
        if(payload.privacy() != RoomPrivacyPolicy.PUBLIC) {
            return;
        }

        boolean updateUI = false;
        final var publicRooms = client.getPublicRooms();

        for(RoomUpdate update : payload.updates()) {
            final String roomID = update.roomID();

            switch(update.type()) {
                case ADD -> {
                    if(!publicRooms.containsKey(roomID)) {
                        publicRooms.put(roomID, update.room());
                        updateUI = true;
                    }
                }
                case DELETE -> {
                    if(publicRooms.remove(roomID) != null) {
                        updateUI = true;
                    }
                }
                case UPDATE -> {
                    if(publicRooms.get(roomID) != null) {
                        publicRooms.put(roomID, update.room());
                        updateUI = true;
                    }
                }
            }
        }

        // Update UI in swing's thread
        if(updateUI) {
            SwingUtilities.invokeLater(client::refreshRoomsListUI);
        }
    }
}
