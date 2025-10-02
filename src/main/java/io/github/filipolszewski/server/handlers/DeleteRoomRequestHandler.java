package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.DeleteRoomPayload;
import io.github.filipolszewski.constants.status.room.RemoveRoomStatus;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.events.impl.RoomCreatedEvent;
import io.github.filipolszewski.server.events.impl.RoomDeletedEvent;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.util.mappers.RoomMapper;

public class DeleteRoomRequestHandler implements RequestHandler {
    @Override
    public Response<DeleteRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and user ID
        final DeleteRoomPayload payload = (DeleteRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to delete a room!", payload);
        }

        // Get data
        final String roomID = payload.roomID();
        final RoomService rs = clientHandler.getContext().roomService();

        // Try to delete the room
        Room roomToDelete = rs.getRoom(roomID);
        RemoveRoomStatus status = rs.removeRoom(roomID, uid);

        // If ok send success, else send failure
        switch(status) {
            case SUCCESS -> {
                // Notify other clients about the new room creation
                clientHandler.getContext().eventBus().emit(new RoomDeletedEvent(RoomMapper.toDTO(roomToDelete)));

                return new Response<>("Successfully deleted room \"" + roomID +"\".", payload);
            }
            case ROOM_NOT_FOUND -> {
                return failure(payload, roomID, "This room does not exist!");
            }
            case NOT_ADMIN -> {
                return failure(payload, roomID, "You don't have enough permissions to delete this room!");
            }
            case NOT_EMPTY -> {
                return failure(payload, roomID, "Room must be emptied before deletion!");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    private Response<DeleteRoomPayload> failure(DeleteRoomPayload payload, String roomID, String reason) {
        return new Response<>(false, "Failed to delete room \"" + roomID + "\". " + reason, payload);
    }
}
