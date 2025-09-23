package io.github.filipolszewski.uicommands;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.view.AppWindow;

import java.io.IOException;

public class JoinRoomCommand implements Command {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    public JoinRoomCommand(Connection<Request<? extends Payload>, Response<? extends Payload>> conn, AppWindow window) {
        this.conn = conn;
        this.window = window;
    }

    public void execute() {
        String roomID = window.promptInputDialog("Enter room ID");
        if(roomID == null) return;

        Request<JoinRoomPayload> req = new Request<>(new JoinRoomPayload(roomID));

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
