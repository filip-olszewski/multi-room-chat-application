package io.github.filipolszewski.communication.leaveroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
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
        clientHandler.getRoomManager().leaveRoom(roomID, uid);

        // Clear the room user is currently in
        user.setCurrentRoomID(null);

        // Broadcast leaving the room
        try {
            clientHandler.getServer().broadcastRoom(roomID, uid + " has left the room");
        } catch (IOException e) {
            log.severe("Could not broadcast the message");
        }

        // Send back success
        return new Response<>("Left the room " + roomID, payload);
    }
}
