package io.github.filipolszewski.uicommands.impl;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.communication.Request;
import io.github.filipolszewski.communication.Response;
import io.github.filipolszewski.communication.fetchrooms.FetchRoomsPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.uicommands.ParamCommand;
import io.github.filipolszewski.view.AppWindow;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class FetchRoomsCommand implements ParamCommand<RoomPrivacyPolicy> {

    private final Connection<Request<? extends Payload>, Response<? extends Payload>> conn;
    private final AppWindow window;

    @Override
    public void execute(RoomPrivacyPolicy privacy) {
        // Send an empty list (we request a fetch)
        Request<FetchRoomsPayload> req = new Request<>(new FetchRoomsPayload(Collections.emptyList(), privacy));

        try {
            conn.send(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
