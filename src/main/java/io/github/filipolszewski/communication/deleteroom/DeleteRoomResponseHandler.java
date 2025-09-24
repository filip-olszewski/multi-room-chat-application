package io.github.filipolszewski.communication.deleteroom;

import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.server.ClientHandler;

public class DeleteRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(response.success()) {
            window.displaySuccessDialog(response.message());
        }
        else {
            window.displayErrorDialog(response.message());
        }
    }
}
