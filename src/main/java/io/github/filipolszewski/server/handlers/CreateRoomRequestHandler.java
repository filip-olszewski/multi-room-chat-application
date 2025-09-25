package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.RoomUpdate;
import io.github.filipolszewski.communication.RoomUpdateType;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.CreateRoomPayload;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.constants.status.room.CreateRoomStatus;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.Server;
import io.github.filipolszewski.server.services.RoomService;

import java.io.IOException;
import java.util.Collections;

public class CreateRoomRequestHandler implements RequestHandler {
    @Override
    public Response<CreateRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final CreateRoomPayload payload = (CreateRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();
        final RoomService rs = clientHandler.getRoomService();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to create a new room!",
                    payload);
        }

        // Check room availability
        CreateRoomStatus status = rs.createRoom(payload.roomID(), uid, payload.capacity(), payload.privacy());

        // If ok then send back success, else send failure
        switch(status) {
            case SUCCESS -> {

                clientHandler.getUserService().getUser(uid).setCurrentRoomID(payload.roomID());
                return new Response<>("Successfully created new room \"" + payload.roomID() + "\".", payload);
            }
            case ALREADY_EXISTS -> {
                return new Response<>(false,
                        "Failed to create the room. Room with this ID already exists!",
                        payload);
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }
}
