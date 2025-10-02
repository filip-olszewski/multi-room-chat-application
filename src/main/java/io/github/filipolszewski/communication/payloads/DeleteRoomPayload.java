package io.github.filipolszewski.communication.payloads;

import io.github.filipolszewski.communication.core.Payload;

public record DeleteRoomPayload(String roomID) implements Payload {
}
