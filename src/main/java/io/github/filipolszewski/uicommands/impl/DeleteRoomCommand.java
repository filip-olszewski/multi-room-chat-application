package io.github.filipolszewski.uicommands.impl;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.deleteroom.DeleteRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.uicommands.NoArgsCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class DeleteRoomCommand implements NoArgsCommand {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    @Override
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
