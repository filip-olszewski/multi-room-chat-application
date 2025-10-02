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
    }
}
