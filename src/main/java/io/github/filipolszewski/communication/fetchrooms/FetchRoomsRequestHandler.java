package io.github.filipolszewski.communication.fetchrooms;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.managers.RoomManager;

import java.util.Collection;

public class FetchRoomsRequestHandler implements RequestHandler {
    @Override
    public Response<FetchRoomsPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final FetchRoomsPayload payload = (FetchRoomsPayload) request.payload();
        final RoomManager rm = clientHandler.getRoomManager();
        final Collection<Room> rooms = rm.getAll(payload.privacy());

        final FetchRoomsPayload newPayload = new FetchRoomsPayload(rooms, payload.privacy());

        // Return a new successful response containing all the rooms
        return new Response<>("Successfully retrieved all public rooms", newPayload);
    }
}
