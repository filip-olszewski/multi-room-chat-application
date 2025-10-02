package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.LeaveRoomPayload;
import io.github.filipolszewski.constants.status.room.LeaveRoomStatus;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.events.impl.RoomModifiedEvent;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.server.services.UserService;
import io.github.filipolszewski.util.mappers.RoomMapper;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class LeaveRoomRequestHandler implements RequestHandler {
    @Override
    public Response<LeaveRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and user ID
        final LeaveRoomPayload payload = (LeaveRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to join a room",
                    payload);
        }

        // Get data
        final UserService us = clientHandler.getContext().userService();
        final RoomService rs = clientHandler.getContext().roomService();
        final User user = us.getUser(uid);
        final String roomID = user.getCurrentRoomID();

        // Leave the room
        LeaveRoomStatus status = rs.leaveRoom(roomID, uid);
        
        switch(status) {
            case SUCCESS -> {
                // Broadcast leaving the room
                try {
                    clientHandler.getServer().broadcastMessageRoom(roomID, uid + " has left the room");
                } catch (IOException e) {
                    log.severe("Could not broadcast the message");
                }

                // Notify other clients about someone leaving the room for a UI update
                Room room = rs.getRoom(roomID);
                clientHandler.getContext().eventBus().emit(new RoomModifiedEvent(RoomMapper.toDTO(room)));

                // Clear the room user is currently in
                user.setCurrentRoomID(null);

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
