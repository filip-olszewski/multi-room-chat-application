package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.services.RoomService;

public class FetchRoomsRequestHandler implements RequestHandler {
    @Override
    public Response<FetchRoomsPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final FetchRoomsPayload payload = (FetchRoomsPayload) request.payload();
        final RoomService rs = clientHandler.getRoomService();


        // final FetchRoomsPayload newPayload = new FetchRoomsPayload(rooms, payload.privacy());

        // Return a new successful response containing all the rooms
        // return new Response<>("Successfully retrieved all public rooms.", newPayload);
        return null;
    }
}
