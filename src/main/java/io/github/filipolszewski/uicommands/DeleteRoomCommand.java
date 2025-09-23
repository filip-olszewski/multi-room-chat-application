package io.github.filipolszewski.uicommands;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.view.AppWindow;

import java.io.IOException;

public class DeleteRoomCommand implements Command {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    public DeleteRoomCommand(Connection<Request<? extends Payload>, Response<? extends Payload>> conn, AppWindow window) {
        this.conn = conn;
        this.window = window;
    }

    public void execute() {
        String roomID = window.promptInputDialog("Enter room ID");
        if(roomID == null) return;

        Request<DeleteRoomPayload> req = new Request<>(new DeleteRoomPayload(roomID));

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
