package io.github.filipolszewski.communication.message;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class MessageRequestHandler implements RequestHandler {
    @Override
    public Response<MessagePayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final MessagePayload payload = (MessagePayload) request.payload();
        final String uid = clientHandler.getUserID();
        final User user = clientHandler.getUserManager().getUser(uid);
        final String roomID = user.getCurrentRoomID();

        // Try to broadcast the message
        try {
            clientHandler.getServer().broadcastRoom(roomID, uid, payload.message());
        } catch (IOException e) {
            // If failed log and send back failure
            log.severe("Could not broadcast message " + payload + " from " + uid);
            return new Response<>(false, "Could not broadcast message.", payload);
        }

        // Send success
        return new Response<>(null, payload);
    }
}
