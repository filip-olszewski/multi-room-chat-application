package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.JoinRoomPayload;
import io.github.filipolszewski.constants.status.room.JoinRoomStatus;
import io.github.filipolszewski.server.ClientHandler;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class JoinRoomRequestHandler implements RequestHandler {
    @Override
    public Response<JoinRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final JoinRoomPayload payload = (JoinRoomPayload) request.payload();
        final String roomID = payload.roomID();
        final String uid = clientHandler.getUserID();

        // Fail if user not registered
        if(uid == null) {
            return new Response<>(false,
                    "Failure. You need to be logged in to join a room.", payload);
        }

        // Check room availability
        JoinRoomStatus status = clientHandler.getRoomService().joinRoom(roomID, uid);

        switch(status) {
            // Success, room was found and there was enough space for user to join
            case SUCCESS -> {
                // Broadcast joining to others
                try {
                    clientHandler.getServer().broadcastMessageRoom(roomID, uid + " has joined the room.");
                } catch (IOException e) {
                    log.severe("Could not broadcast the message");
                }

                // Set room id for client handler
                clientHandler.getUserService().getUser(uid).setCurrentRoomID(roomID);

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
