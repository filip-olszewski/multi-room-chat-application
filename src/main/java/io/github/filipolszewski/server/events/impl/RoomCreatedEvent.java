package io.github.filipolszewski.server.events.impl;

import io.github.filipolszewski.dto.RoomDTO;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.server.events.Event;

public record RoomCreatedEvent(RoomDTO room) implements Event {
}
