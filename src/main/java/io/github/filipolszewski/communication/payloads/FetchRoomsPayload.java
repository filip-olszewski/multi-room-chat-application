package io.github.filipolszewski.communication.payloads;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import java.util.Collection;

public record FetchRoomsPayload(Collection<Room> rooms, RoomPrivacyPolicy privacy) implements Payload {
}
