package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.uicommands.impl.FetchRoomsCommand;
import io.github.filipolszewski.uicommands.impl.MessageCommand;
import io.github.filipolszewski.view.screens.HomeScreen;
import lombok.extern.java.Log;

@Log
public class LoginResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            client.requestLogin();
            return;
        }

        // Get login payload
        LoginPayload payload = (LoginPayload) response.payload();

        // Create and set new user for client
        client.setUser(new User(payload.userID()));

        // Show success message and redirect
        window.displaySuccessDialog(response.message());
        window.showScreen(HomeScreen.HOME_SCREEN_KEY);

        // Fetch public rooms to rooms list
        client.getCommandRegistry().getParam(FetchRoomsCommand.class).execute(RoomPrivacyPolicy.PUBLIC);
    }
}
