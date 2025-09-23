package io.github.filipolszewski.communication.joinroom;

import io.github.filipolszewski.communication.Payload;

public record JoinRoomPayload(String roomID) implements Payload {
}
