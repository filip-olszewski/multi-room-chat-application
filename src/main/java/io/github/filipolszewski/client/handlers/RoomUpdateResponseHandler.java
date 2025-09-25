package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.RoomUpdatePayload;
import io.github.filipolszewski.dto.RoomDTO;

import javax.swing.*;

public class RoomUpdateResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {

        final RoomUpdatePayload payload = (RoomUpdatePayload) response.payload();
        final RoomDTO room = payload.room();

        System.out.println(room);

        switch(payload.type()) {
            case CREATE -> {
                System.out.println("CREATED");
                client.getPublicRooms().put(room.roomID(), room);
                client.addRoomListingToUI(room);
            }
            case DELETE -> {
                client.getPublicRooms().remove(room.roomID());
                System.out.println("DELETED");
            }
            case MODIFY -> {
                client.getPublicRooms().computeIfPresent(room.roomID(), (id, r) -> room);
                System.out.println("MODIFIED");
            }
        }

        SwingUtilities.invokeLater(client::refreshRoomsListUI);
    }
}
