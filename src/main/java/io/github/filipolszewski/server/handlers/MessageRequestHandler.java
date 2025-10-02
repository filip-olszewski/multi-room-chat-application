package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.MessagePayload;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.services.UserService;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class MessageRequestHandler implements RequestHandler {
    @Override
    public Response<MessagePayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final MessagePayload payload = (MessagePayload) request.payload();
        final String uid = clientHandler.getUserID();
        final UserService us = clientHandler.getContext().userService();
        final User user = us.getUser(uid);
        final String roomID = user.getCurrentRoomID();

        // Try to broadcast the message
        try {
            clientHandler.getServer().broadcastMessageRoom(roomID, uid, payload.message());
        } catch (IOException e) {
            // If failed log and send back failure
            log.severe("Could not broadcast message " + payload + " from " + uid);
            return new Response<>(false, "Could not broadcast message.", payload);
        }

        // Send success
        return new Response<>(null, payload);
    }
}
