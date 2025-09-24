package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.server.managers.UserManager;

public class LoginRequestHandler implements RequestHandler {
    @Override
    public Response<LoginPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {
        // Get the payload
        LoginPayload payload = (LoginPayload) request.payload();

        // Prepare response
        Response<LoginPayload> res = null;

        // Get userID
        String uid = payload.userID();
        UserManager um = clientHandler.getUserManagerRef();

        // Get user from user manager
        User user = um.getUser(uid);
        boolean ok = false;

        // If user does not exist create new user
        if(user == null) {
            user = new User(uid);
            ok = um.addUser(user);
        }

        // If ok send success
        if(ok) {
            // Set user for the client handler
            clientHandler.setUser(user);

            res = new Response<>(
                    "Successfully registered as " + uid,
                    new LoginPayload(uid));
        }
        // If not send failure
        else {
            res = new Response<>(
                    false,
                    "Failed to register as " + uid,
                    new LoginPayload(uid));
        }

        return res;
    }
}
