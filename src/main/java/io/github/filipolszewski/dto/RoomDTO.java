package io.github.filipolszewski.dto;

import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import java.io.Serializable;
import java.util.Set;

public record RoomDTO
    (String roomID, String adminID, int capacity, RoomPrivacyPolicy privacy, Set<String> activeUsers)
    implements Serializable {}
