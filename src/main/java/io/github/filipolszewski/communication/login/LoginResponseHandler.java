package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.view.screens.HomeScreen;
import lombok.extern.java.Log;

@Log
public class LoginResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(response.success()) {
            // Get login payload
            LoginPayload payload = (LoginPayload) response.payload();

            // Create new user
            User user = new User(payload.userID());

            // Set the user for client
            client.setLoggedInUser(user);

            // Show success message and redirect
            window.displaySuccessDialog(response.message());
            window.showScreen(HomeScreen.HOME_SCREEN_KEY);
        }
        else {
            window.displayErrorDialog(response.message());
            client.requestLogin();
        }
    }
}
