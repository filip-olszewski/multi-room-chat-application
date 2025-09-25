package io.github.filipolszewski.uicommands.impl;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.uicommands.NoArgsCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class CreateRoomCommand implements NoArgsCommand {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    @Override
    public void execute() {
        CreateRoomPayload payload = window.promptCreateRoom("Create new room");
        if(payload == null) {
            return;
        }
        if(payload.roomID().trim().isEmpty()) {
            window.displayErrorDialog("RoomID cannot be empty");
        }

        Request<CreateRoomPayload> req = new Request<>(payload);

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
