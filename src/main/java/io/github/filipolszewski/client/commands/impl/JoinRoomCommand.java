package io.github.filipolszewski.client.commands.impl;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.JoinRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.client.commands.ParamCommand;
import io.github.filipolszewski.client.commands.NoArgsCommand;
import io.github.filipolszewski.client.ui.AppWindow;
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
