package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.RequestHandler;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;

public class LoginRequestHandler implements RequestHandler {
    @SuppressWarnings("unchecked")
    @Override
    public Response<LoginPayload> handle(Request<? extends Payload> request, ClientHandler clientHandler) {

        // Cast request to be login type
        Request<LoginPayload> req = (Request<LoginPayload>) request;

        // Prepare response
        Response<LoginPayload> res = null;

        // Get userID
        String uid = req.payload().userID();


        // Create new user and check availability
        var user = new User(uid);
        boolean ok = clientHandler.getUserManagerRef().addUser(user);

        // If ok set the user for the ClientHandler and send success
        if(ok) {
            clientHandler.setUser(user);
            res = new Response<>("Successfully registered as " + uid, new LoginPayload(uid));
        }
        // If not send failure
        else {
            res = new Response<>(false, "Failed to register as " + uid, null);
        }

        return res;
    }
}
