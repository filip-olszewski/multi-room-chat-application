package io.github.filipolszewski.communication.deleteroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.server.ClientHandler;

public class DeleteRoomRequestHandler implements RequestHandler {
    @Override
    public Response<DeleteRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {
        // Get payload
        DeleteRoomPayload payload = (DeleteRoomPayload) request.payload();

        // Prepare response
        Response<DeleteRoomPayload> res = null;

        // Get room id from the request
        String roomID = payload.roomID();

        // Get user from the server
        var user = clientHandler.getUser();

        // Fail if user not registered
        if(user == null) {
            res = new Response<>(false,
                    "Failure. You need to be logged in to delete a room",
                    new DeleteRoomPayload(roomID));
            return res;
        }

        // Get userID
        String uid = user.getUserID();

        System.out.println(uid);
        System.out.println(roomID);

        // Try to delete the room
        boolean ok = clientHandler.getRoomManagerRef().removeRoom(roomID, uid);

        System.out.println(ok);

        // If possible, send success
        if(ok) {
            res = new Response<>(
                    "Successfully deleted room " + roomID,
                    new DeleteRoomPayload(roomID));
        }
        // Else send failure
        else {
            res = new Response<>(false,
                    "Failed to delete room " + roomID,
                    new DeleteRoomPayload(roomID));
        }

        return res;
    }
}
