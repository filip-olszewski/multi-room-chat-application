package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.LeaveRoomPayload;
import io.github.filipolszewski.constants.status.room.LeaveRoomStatus;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class LeaveRoomRequestHandler implements RequestHandler {
    @Override
    public Response<LeaveRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final LeaveRoomPayload payload = (LeaveRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to join a room",
                    payload);
        }

        // Get current roomID and user
        final User user = clientHandler.getUserManager().getUser(uid);
        final String roomID = user.getCurrentRoomID();

        // Leave the room
        LeaveRoomStatus status = clientHandler.getRoomManager().leaveRoom(roomID, uid);
        
        switch(status) {
            case SUCCESS -> {
                // Clear the room user is currently in
                user.setCurrentRoomID(null);

                // Broadcast leaving the room
                try {
                    clientHandler.getServer().broadcastRoom(roomID, uid + " has left the room");
                } catch (IOException e) {
                    log.severe("Could not broadcast the message");
                }

                // Send back success
                return new Response<>("Left the room \"" + roomID + "\".", payload);
            }
            case ROOM_NOT_FOUND -> {
                return failure(payload, roomID, "Room does not exist!");
            }
            case USER_NOT_IN_ROOM -> {
                return failure(payload, roomID, "You are not joined to any rooms!");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    private Response<LeaveRoomPayload> failure(LeaveRoomPayload payload, String roomID, String reason) {
        return new Response<>(false, "Failed to leave the room \"" + roomID + "\". " + reason, payload);
    }
}
