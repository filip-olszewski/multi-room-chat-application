package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.JoinRoomPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.dto.RoomDTO;

public class JoinRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        final JoinRoomPayload payload = (JoinRoomPayload) response.payload();

        // Change the screen and display success dialog
        // TODO: implement handling actual RoomDTOs
        window.showChatScreen(new RoomDTO(payload.roomID(), "test admin", 0, RoomPrivacyPolicy.PRIVATE, null));
        window.displaySuccessDialog(response.message());

        // Set current room ID for user on the client side
        client.getUser().setCurrentRoomID(payload.roomID());
    }
}
