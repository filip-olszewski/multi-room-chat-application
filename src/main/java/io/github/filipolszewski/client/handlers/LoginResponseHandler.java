package io.github.filipolszewski.client.handlers;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.client.commands.impl.LoginCommand;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.core.ResponseHandler;
import io.github.filipolszewski.communication.payloads.LoginPayload;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.model.user.User;
import io.github.filipolszewski.client.commands.impl.FetchRoomsCommand;
import lombok.extern.java.Log;

@Log
public class LoginResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        final var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            client.getCommandRegistry().getNoArgs(LoginCommand.class).execute();
            return;
        }

        // Get login payload
        final LoginPayload payload = (LoginPayload) response.payload();

        // Create and set new user for client
        client.setUser(new User(payload.userID()));

        // Show success message and redirect
        window.displaySuccessDialog(response.message());
        window.showHomeScreen();
    }
}
