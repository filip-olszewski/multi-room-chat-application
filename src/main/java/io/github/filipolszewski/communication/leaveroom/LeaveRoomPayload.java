package io.github.filipolszewski.communication.leaveroom;

import io.github.filipolszewski.communication.Payload;

public record LeaveRoomPayload(String roomID) implements Payload {
}
