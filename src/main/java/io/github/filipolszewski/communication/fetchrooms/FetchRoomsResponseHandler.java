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

        if(response.success()) {
            // Get the payload
            FetchRoomsPayload payload = (FetchRoomsPayload) response.payload();

            // Cast the rooms
            List<Room> rooms = (List<Room>) payload.rooms();

            // Put rooms in the correct privacy
            client.getRooms().put(payload.privacy(), new ArrayList<>(rooms));


            // If privacy is public, add to the list
            if(payload.privacy() == RoomPrivacyPolicy.PUBLIC) {
                // Update UI in swing's thread
                SwingUtilities.invokeLater(client::refreshRoomsList);
            }
        }
        else {
            window.displayErrorDialog(response.message());
        }
    }
}
