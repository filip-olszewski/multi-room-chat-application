package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.DeleteRoomPayload;
import io.github.filipolszewski.constants.status.room.RemoveRoomStatus;
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
                    "Failure. You need to be logged in to delete a room!", payload);
        }

        // Try to delete the room
        RemoveRoomStatus status = clientHandler.getRoomManager().removeRoom(roomID, uid);

        // If ok send success, else send failure
        return switch(status) {
            case SUCCESS -> new Response<>("Successfully deleted room \"" + roomID +"\".", payload);
            case ROOM_NOT_FOUND -> failure(payload, roomID, "This room does not exist!");
            case NOT_ADMIN -> failure(payload, roomID, "You don't have enough permissions to delete this room!");
            case NOT_EMPTY -> failure(payload, roomID, "Room must be emptied before deletion!");
        };

    }

    private Response<DeleteRoomPayload> failure(DeleteRoomPayload payload, String roomID, String reason) {
        return new Response<>(false, "Failed to delete room \"" + roomID + "\". " + reason, payload);
    }
}
