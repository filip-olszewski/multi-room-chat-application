package io.github.filipolszewski.uicommands.impl;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.leaveroom.LeaveRoomPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.uicommands.NoArgsCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.io.IOException;

@RequiredArgsConstructor
public class LeaveRoomCommand implements NoArgsCommand {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    @Override
    public void execute() {
        int confirmation = window.displayConfirmDialog("Are you sure you want to leave the room?");
        if(confirmation != JOptionPane.OK_OPTION) return;

        Request<LeaveRoomPayload> req = new Request<>(new LeaveRoomPayload());

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
