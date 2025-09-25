package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.CreateRoomPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.model.room.Room;

import javax.swing.*;

public class CreateRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        // Display dialog
        window.displaySuccessDialog(response.message());

        // FIXME: maybe delete and delegate this behaviour to fetch

        /*
        // Get payload and other data
        final CreateRoomPayload payload = (CreateRoomPayload) response.payload();
        final String uid = client.getUser().getUserID();
        final String roomID = payload.roomID();
        final int capacity = payload.capacity();
        final RoomPrivacyPolicy privacy = payload.privacy();

        Room room = new Room(roomID, uid, capacity, privacy);

        // Add room to public room map
        if(privacy == RoomPrivacyPolicy.PUBLIC) {
            client.getPublicRooms().put(roomID, room);
        }

        // Add room to UI
        SwingUtilities.invokeLater(() -> {
            client.addRoomListingToUI(room);
        });
        */
    }
}
