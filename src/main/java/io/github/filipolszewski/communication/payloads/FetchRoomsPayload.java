package io.github.filipolszewski.communication.payloads;

import io.github.filipolszewski.communication.RoomUpdate;
import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import java.util.Collection;
import java.util.List;

public record FetchRoomsPayload(List<RoomUpdate> updates, RoomPrivacyPolicy privacy) implements Payload {
}
