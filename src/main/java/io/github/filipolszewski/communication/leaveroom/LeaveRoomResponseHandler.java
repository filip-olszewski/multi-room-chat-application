package io.github.filipolszewski.communication.leaveroom;

import com.sun.java.accessibility.util.AccessibilityListenerList;
import io.github.filipolszewski.client.Client;
import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.server.ClientHandler;
import io.github.filipolszewski.view.AppWindow;
import io.github.filipolszewski.view.screens.HomeScreen;

public class LeaveRoomResponseHandler implements ResponseHandler {
    @Override
    public void handle(Response<? extends Payload> response, Client client) {
        var window = client.getWindow();

        if(!response.success()) {
            window.displayErrorDialog(response.message());
            return;
        }

        window.getChatScreen().clearChat();
        window.showScreen(HomeScreen.HOME_SCREEN_KEY);
        window.displaySuccessDialog(response.message());

        client.getUser().setCurrentRoomID(null);
    }
}
