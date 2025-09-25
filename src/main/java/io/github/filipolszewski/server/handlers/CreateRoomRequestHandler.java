package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.CreateRoomPayload;
import io.github.filipolszewski.constants.status.room.CreateRoomStatus;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.events.impl.RoomCreatedEvent;
import io.github.filipolszewski.server.events.impl.RoomDeletedEvent;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.server.services.UserService;
import io.github.filipolszewski.util.mappers.RoomMapper;

public class CreateRoomRequestHandler implements RequestHandler {
    @Override
    public Response<CreateRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and user ID
        final CreateRoomPayload payload = (CreateRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to create a new room!",
                    payload);
        }

        // Get data
        final RoomService rs = clientHandler.getContext().roomService();
        final UserService us = clientHandler.getContext().userService();

        // Check room availability
        Room room = new Room(payload.roomID(), uid, payload.capacity(), payload.privacy());
        CreateRoomStatus status = rs.createRoom(room);

        // If ok then send back success, else send failure
        switch(status) {
            case SUCCESS -> {
                // Notify other clients about the new room creation
                clientHandler.getContext().eventBus().emit(new RoomCreatedEvent(RoomMapper.toDTO(room)));
                // Set room id for the user
                us.getUser(uid).setCurrentRoomID(payload.roomID());
                // Return success
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
