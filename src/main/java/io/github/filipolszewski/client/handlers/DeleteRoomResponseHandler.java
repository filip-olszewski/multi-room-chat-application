package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.DeleteRoomPayload;

import javax.swing.*;

public class DeleteRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        // Show success
        window.displaySuccessDialog(response.message());

        final DeleteRoomPayload payload = (DeleteRoomPayload) response.payload();

        // Delete the room from the public room list
        client.getPublicRooms().remove(payload.roomID());

        SwingUtilities.invokeLater(client::refreshRoomsListUI);
    }
}
