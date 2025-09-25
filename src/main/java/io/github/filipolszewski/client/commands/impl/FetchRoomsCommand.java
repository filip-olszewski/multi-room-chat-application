package io.github.filipolszewski.client.commands.impl;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.communication.core.Request;
import io.github.filipolszewski.communication.core.Response;
import io.github.filipolszewski.communication.payloads.FetchRoomsPayload;
import io.github.filipolszewski.connection.Connection;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;
import io.github.filipolszewski.client.commands.ParamCommand;
import io.github.filipolszewski.client.ui.AppWindow;
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
