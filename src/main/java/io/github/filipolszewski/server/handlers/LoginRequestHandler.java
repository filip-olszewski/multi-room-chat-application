package io.github.filipolszewski.server.handlers;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.RequestHandler;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.LoginPayload;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.Server;
import io.github.filipolszewski.server.services.UserService;

public class LoginRequestHandler implements RequestHandler {
    @Override
    public Response<LoginPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final LoginPayload payload = (LoginPayload) request.payload();
        final String uid = payload.userID();
        final UserService us = clientHandler.getUserService();
        final Server server = clientHandler.getServer();

        // Check if user is already in the clients map
        if(server.getClientHandler(uid) != null) {
            return new Response<>(false, "User \"" + uid + "\" already exists!", payload);
        }

        // Get user from user manager
        User user = us.getOrAddUser(uid);

        // If user does not exist create new user
        if (user == null) {
            return new Response<>(false, "Failed to log in user \"" + uid + "\".", payload);
        }

        // Add clientHandler to the map
        server.addClientHandler(uid, clientHandler);

        // Add new user to the user manager
        us.addUser(user);

        // Set id of current user clientHandler is handling
        clientHandler.setUserID(uid);

        return new Response<>("Successfully logged in as \"" + uid + "\".", payload);
    }
}
