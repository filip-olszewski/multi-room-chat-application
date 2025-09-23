package io.github.filipolszewski.uicommands;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.view.AppWindow;

import java.io.IOException;

public class CreateRoomCommand implements Command {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    public CreateRoomCommand(Connection<Request<? extends Payload>, Response<? extends Payload>> conn, AppWindow window) {
        this.conn = conn;
        this.window = window;
    }

    public void execute() {
        String roomID = window.promptInputDialog("Enter new room ID");
        if(roomID == null) return;

        Request<CreateRoomPayload> req = new Request<>(new CreateRoomPayload(roomID));

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
