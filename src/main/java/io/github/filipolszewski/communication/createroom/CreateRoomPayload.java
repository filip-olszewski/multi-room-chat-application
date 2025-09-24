package io.github.filipolszewski.communication.createroom;

import io.github.filipolszewski.communication.Payload;
import io.github.filipolszewski.constants.RoomConfig;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

public record CreateRoomPayload(String roomID, int capacity, RoomPrivacyPolicy privacy) implements Payload {
    public CreateRoomPayload(String roomID) {
        this(roomID, RoomConfig.DEFAULT_ROOM_CAPACITY, RoomConfig.DEFAULT_PRIVACY_POLICY);
    }
}
