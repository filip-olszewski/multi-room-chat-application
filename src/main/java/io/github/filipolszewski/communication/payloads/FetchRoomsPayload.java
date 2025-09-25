package io.github.filipolszewski.communication.payloads;

import io.github.filipolszewski.communication.core.Payload;
import io.github.filipolszewski.dto.RoomDTO;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import java.util.List;

public record FetchRoomsPayload(List<RoomDTO> rooms, RoomPrivacyPolicy privacy) implements Payload {
}
