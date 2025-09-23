package io.github.filipolszewski.communication.leaveroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.server.ClientHandler;

public class LeaveRoomRequestHandler implements RequestHandler {
    @SuppressWarnings("unchecked")
    @Override
    public Response<LeaveRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Cast request to CreateRoom
        Request<LeaveRoomPayload> req = (Request<LeaveRoomPayload>) request;

        // Prepare response
        Response<LeaveRoomPayload> res = null;

        // Extract data
        String roomID = req.payload().roomID();

        var user = clientHandler.getUser();

        if(user == null) {
            res = new Response<>(false, "Failure. You need to be logged in to join a room", null);
            return res;
        }

        String uid = user.getUserID();

        // Leave the room
        clientHandler.getRoomManagerRef().leaveRoom(roomID, uid);
        res = new Response<>("Left the room " + roomID, new LeaveRoomPayload(roomID));

        return res;
    }
}
