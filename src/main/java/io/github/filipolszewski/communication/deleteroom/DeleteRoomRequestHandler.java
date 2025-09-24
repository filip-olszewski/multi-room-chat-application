package io.github.filipolszewski.communication.deleteroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.server.ClientHandler;

public class DeleteRoomRequestHandler implements RequestHandler {
    @Override
    public Response<DeleteRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final DeleteRoomPayload payload = (DeleteRoomPayload) request.payload();
        final String roomID = payload.roomID();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to delete a room", payload);
        }

        // Try to delete the room
        boolean ok = clientHandler.getRoomManager().removeRoom(roomID, uid);

        // If ok send success, else send failure
        if(ok) {
            return new Response<>("Successfully deleted room " + roomID, payload);
        }
        else {
            return new Response<>(false,
                    "Failed to delete room " + roomID,
                    payload);
        }
    }
}
