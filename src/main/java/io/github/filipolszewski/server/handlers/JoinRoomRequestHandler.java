package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.JoinRoomPayload;
import io.github.filipolszewski.constants.status.room.JoinRoomStatus;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.events.impl.RoomCreatedEvent;
import io.github.filipolszewski.server.events.impl.RoomModifiedEvent;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.server.services.UserService;
import io.github.filipolszewski.util.mappers.RoomMapper;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class JoinRoomRequestHandler implements RequestHandler {
    @Override
    public Response<JoinRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and user ID
        final JoinRoomPayload payload = (JoinRoomPayload) request.payload();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to join a room.", payload);
        }

        // Get data
        final String roomID = payload.roomID();
        final UserService us = clientHandler.getContext().userService();
        final RoomService rs = clientHandler.getContext().roomService();

        // Check room availability
        JoinRoomStatus status = rs.joinRoom(roomID, uid);

        switch(status) {
            // Success, room was found and there was enough space for user to join
            case SUCCESS -> {
                // Broadcast joining to others
                try {
                    clientHandler.getServer().broadcastMessageRoom(roomID, uid + " has joined the room.");
                } catch (IOException e) {
                    log.severe("Could not broadcast the message");
                }

                System.out.println(rs.getRoom(roomID));

                // Notify other clients about someone joining the room for a UI update
                Room room = rs.getRoom(roomID);
                clientHandler.getContext().eventBus().emit(new RoomModifiedEvent(RoomMapper.toDTO(room)));

                // Set room id for client handler
                us.getUser(uid).setCurrentRoomID(roomID);

                // Send success
                return new Response<>("Successfully joined the room \"" + roomID + "\".", payload);
            }
            case ROOM_NOT_FOUND -> {
                return failure(payload, roomID, "This room does not exists!");
            }
            case FULL -> {
                return failure(payload, roomID, "This room is currently full!");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    private Response<JoinRoomPayload> failure(JoinRoomPayload payload, String roomID, String reason) {
        return new Response<>(false, "Failed to join the room \"" + roomID + "\". " + reason, payload);
    }
}
