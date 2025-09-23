package io.github.filipolszewski.communication.deleteroom;

import io.github.filipolszewski.communication.Payload;

public record DeleteRoomPayload(String roomID) implements Payload {
}
