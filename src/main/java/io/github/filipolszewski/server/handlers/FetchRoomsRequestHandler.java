package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.dto.RoomDTO;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.util.mappers.RoomMapper;

import java.util.List;

public class FetchRoomsRequestHandler implements RequestHandler {
    @Override
    public Response<FetchRoomsPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final FetchRoomsPayload payload = (FetchRoomsPayload) request.payload();
        final RoomService rs = clientHandler.getContext().roomService();

        // Transform rooms to DTO representation
        List<RoomDTO> roomDTOs = rs.getAll().stream()
                .map(RoomMapper::toDTO) // Transform all the room model objects to roomDTOs
                .filter(r -> r.privacy() == payload.privacy()) // Filter by privacy
                .toList(); // Accumulate elements back into a List

        FetchRoomsPayload newPayload = new FetchRoomsPayload(roomDTOs, payload.privacy());

        // Return a new successful response containing all the rooms
        return new Response<>("Successfully retrieved all public rooms.", newPayload);
    }
}
