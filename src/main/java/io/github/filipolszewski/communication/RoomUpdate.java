package io.github.filipolszewski.communication;

import io.github.filipolszewski.model.room.Room;

import java.io.Serializable;

public record RoomUpdate(String roomID, Room room, RoomUpdateType type) implements Serializable {
}
