package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.Server;
import io.github.filipolszewski.server.managers.UserManager;

public class LoginRequestHandler implements RequestHandler {
    @Override
    public Response<LoginPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Get payload and data
        final LoginPayload payload = (LoginPayload) request.payload();
        final String uid = payload.userID();
        final UserManager um = clientHandler.getUserManager();
        final Server server = clientHandler.getServer();

        // Check if user is already in the clients map
        if(server.getClientHandler(uid) != null) {
            return new Response<>(false, "User " + uid + " is already logged in!", payload);
        }

        // Get user from user manager
        User user = um.getOrAddUser(uid);

        // If user does not exist create new user
        if (user == null) {
            return new Response<>(false, "Failed to log in user " + uid, payload);
        }

        // Add clientHandler to the map
        server.addClientHandler(uid, clientHandler);

        // Add new user to the user manager
        um.addUser(user);

        // Set id of current user clientHandler is handling
        clientHandler.setUserID(uid);

        return new Response<>("Successfully logged in as " + uid, payload);
    }
}
