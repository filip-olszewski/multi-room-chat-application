package io.github.filipolszewski.uicommands.impl;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.joinroom.JoinRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.uicommands.ParamCommand;
import io.github.filipolszewski.uicommands.NoArgsCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class JoinRoomCommand implements ParamCommand<String>, NoArgsCommand {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;


    @Override
    public void execute() {
        String roomID = window.promptInputDialog("Enter room ID");
        if (roomID != null) {
            execute(roomID);
        }
    }

    @Override
    public void execute(String roomID) {
        if (roomID == null || roomID.isEmpty()) return;

        Request<JoinRoomPayload> req = new Request<>(new JoinRoomPayload(roomID));
        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
