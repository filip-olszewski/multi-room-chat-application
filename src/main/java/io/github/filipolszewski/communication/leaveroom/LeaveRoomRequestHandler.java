package io.github.filipolszewski.communication.leaveroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.server.ClientHandler;

public class LeaveRoomRequestHandler implements RequestHandler {
    @Override
    public Response<LeaveRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {
        // Prepare response
        Response<LeaveRoomPayload> res = null;

        // Get user from the server
        var user = clientHandler.getUser();

        // Fail if user not registered
        if(user == null) {
            res = new Response<>(false,
                    "Failure. You need to be logged in to join a room",
                    new LeaveRoomPayload());
            return res;
        }

        // Get current roomID and userID
        String roomID = user.getCurrentRoomID();
        String uid = user.getUserID();

        // Leave the room
        clientHandler.getRoomManagerRef().leaveRoom(roomID, uid);

        // Clear the room user is currently in
        user.setCurrentRoomID(null);

        // Send back success
        res = new Response<>("Left the room " + roomID, new LeaveRoomPayload());

        return res;
    }
}
