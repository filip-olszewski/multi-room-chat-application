package io.github.filipolszewski.communication.joinroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
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
                    "Failure. You need to be logged in to join a room", payload);
        }

        // Check room availability
        boolean ok = clientHandler.getRoomManager().joinRoom(roomID, uid);

        // If ok then send back success and set current room for server side user
        if(ok) {
            // Broadcast joining to others
            try {
                clientHandler.getServer().broadcastRoom(roomID, uid + " has joined the room.");
            } catch (IOException e) {
                log.severe("Could not broadcast the message");
            }

            clientHandler.getUserManager().getUser(uid).setCurrentRoomID(roomID);
            return new Response<>("Successfully joined the room " + roomID, payload);
        }
        // If not send failure
        else {
            return new Response<>(false,
                    "Failed to join the room. This room either does not exist or it's full.", payload);
        }
    }
}
