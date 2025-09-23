package io.github.filipolszewski.communication.joinroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.server.ClientHandler;

public class JoinRoomRequestHandler implements RequestHandler {
    @SuppressWarnings("unchecked")
    @Override
    public Response<JoinRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Cast request to CreateRoom
        Request<JoinRoomPayload> req = (Request<JoinRoomPayload>) request;

        // Prepare response
        Response<JoinRoomPayload> res = null;

        // Extract data
        String roomID = req.payload().roomID();

        var user = clientHandler.getUser();

        if(user == null) {
            res = new Response<>(false, "Failure. You need to be logged in to join a room", null);
            return res;
        }

        String uid = user.getUserID();


        // Check room availability
        boolean ok = clientHandler.getRoomManagerRef().joinRoom(roomID, uid);

        // If ok then send back success
        if(ok) {
            res = new Response<>("Successfully joined the room " + roomID, new JoinRoomPayload(roomID));
        }
        // If not send failure
        else {
            res = new Response<>(false,
                    "Failed to join the room. This room either does not exist or it's full.",
                    null);
        }


        return res;
    }
}
