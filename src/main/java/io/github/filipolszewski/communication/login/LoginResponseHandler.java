package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.uicommands.MessageCommand;
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

            // Create and set new user for client
            client.setLoggedInUser(new User(payload.userID()));

            // Register message command
            // Needs the user to be set on the client side to work properly
            // (Hence initialization here not in the init method)
            MessageCommand messageCommand = new MessageCommand(client.getConn(), window, payload.userID());
            window.getChatScreen().addSendButtonActionListener(e -> {
                messageCommand.execute();
            });

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
