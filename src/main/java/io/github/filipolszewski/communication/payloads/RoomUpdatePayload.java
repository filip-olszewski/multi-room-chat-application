package io.github.filipolszewski.communication.payloads;

import io.github.filipolszewski.communication.RoomUpdateType;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.dto.RoomDTO;

public record RoomUpdatePayload(RoomDTO room, RoomUpdateType type) implements Payload {
}
