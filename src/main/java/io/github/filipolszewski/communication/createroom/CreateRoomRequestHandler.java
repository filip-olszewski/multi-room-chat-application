package io.github.filipolszewski.communication.createroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.managers.RoomManager;

public class CreateRoomRequestHandler implements RequestHandler {
    @Override
    public Response<CreateRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final CreateRoomPayload payload = (CreateRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();
        final RoomManager rm = clientHandler.getRoomManager();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to create a room",
                    payload);
        }

        // Check room availability
        boolean ok = rm.createAndJoinRoom(payload.roomID(), uid, payload.capacity(), payload.privacy());

        // If ok then send back success, else send failure
        if(ok) {
            clientHandler.getUserManager().getUser(uid).setCurrentRoomID(payload.roomID());
            return new Response<>("Successfully created new room " + payload.roomID(), payload);
        }
        else {
            return new Response<>(false,
                    "Failed to create the room. Room with this ID already exists!",
                    payload);
        }
    }
}
