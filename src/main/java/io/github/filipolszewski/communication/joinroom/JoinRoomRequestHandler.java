package io.github.filipolszewski.communication.joinroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.server.ClientHandler;

public class JoinRoomRequestHandler implements RequestHandler {
    @Override
    public Response<JoinRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {
        // Get paylaod
        JoinRoomPayload payload = (JoinRoomPayload) request.payload();

        // Prepare response
        Response<JoinRoomPayload> res = null;

        // Extract data
        String roomID = payload.roomID();

        // Get user from the server
        var user = clientHandler.getUser();

        // Fail if user not registered
        if(user == null) {
            res = new Response<>(false,
                    "Failure. You need to be logged in to join a room",
                    new JoinRoomPayload(roomID));
            return res;
        }

        // Get user id
        String uid = user.getUserID();

        // Check room availability
        boolean ok = clientHandler.getRoomManagerRef().joinRoom(roomID, uid);

        // If ok then send back success and set current room for server side user
        if(ok) {
            user.setCurrentRoomID(roomID);
            res = new Response<>("Successfully joined the room " + roomID, new JoinRoomPayload(roomID));
        }
        // If not send failure
        else {
            res = new Response<>(false,
                    "Failed to join the room. This room either does not exist or it's full.",
                    new JoinRoomPayload(roomID));
        }

        return res;
    }
}
