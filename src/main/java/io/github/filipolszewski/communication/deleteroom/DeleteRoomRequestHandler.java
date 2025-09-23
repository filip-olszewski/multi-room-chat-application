package io.github.filipolszewski.communication.deleteroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.server.ClientHandler;

public class DeleteRoomRequestHandler implements RequestHandler {
    @SuppressWarnings("unchecked")
    @Override
    public Response<DeleteRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Cast request to DeleteRoom
        Request<DeleteRoomPayload> req = (Request<DeleteRoomPayload>) request;

        // Prepare response
        Response<DeleteRoomPayload> res = null;

        // Extract data
        String roomID = req.payload().roomID();
        var user = clientHandler.getUser();

        if(user == null) {
            res = new Response<>(false, "Failure. You need to be logged in to delete a room", null);
            return res;
        }

        String uid = user.getUserID();

        // Try to delete the room
        boolean ok = clientHandler.getRoomManagerRef().removeRoom(roomID, uid);
        if(ok) {
            res = new Response<>("Successfully deleted room " + roomID, new DeleteRoomPayload(roomID));
        }
        else {
            res = new Response<>(false, "Failed to delete room " + roomID, null);
        }

        return res;
    }
}
