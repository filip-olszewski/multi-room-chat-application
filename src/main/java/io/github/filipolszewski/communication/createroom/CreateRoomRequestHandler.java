package io.github.filipolszewski.communication.createroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.model.room.RoomPrivacyPolicy;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.managers.RoomManager;

public class CreateRoomRequestHandler implements RequestHandler {
    @SuppressWarnings("unchecked")
    @Override
    public Response<CreateRoomPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {
        // Get payload
        CreateRoomPayload payload = (CreateRoomPayload) request.payload();

        // Prepare response
        Response<CreateRoomPayload> res = null;

        // Extract data
        String roomID =             payload.roomID();
        int capacity =              payload.capacity();
        RoomPrivacyPolicy privacy = payload.privacy();

        // Get user from the server
        var user = clientHandler.getUser();

        // Fail if user not registered
        if(user == null) {
            res = new Response<>(false, "Failure. You need to be logged in to create a room", null);
            return res;
        }

        // Get user ID
        String uid = user.getUserID();

        // Get room manager
        RoomManager rm = clientHandler.getRoomManagerRef();


        // Check room availability
        boolean ok = rm.addRoom(new Room(roomID, uid, capacity, privacy));

        // Create room payload
        var roomPayload = new CreateRoomPayload(roomID, capacity, privacy);


        // If ok then send back success, join the room
        if(ok) {
            res = new Response<>(
                    "Successfully created new room " + roomID,
                    roomPayload);
            rm.joinRoom(roomID, uid);
            user.setCurrentRoomID(roomID);
        }
        // If not send failure
        else {
            res = new Response<>(false,
                    "Failed to create the room. Room with this ID already exists!",
                    roomPayload);
        }

        return res;
    }
}
