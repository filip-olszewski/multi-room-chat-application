package io.github.filipolszewski.communication.message;

import io.github.filipolszewski.communication.Payload;

public record MessagePayload(String message, String senderID, boolean isSystemMessage) implements Payload {
    public MessagePayload(String message, String senderID) {
        this(message, senderID, false);
    }
    public MessagePayload(String message, boolean isSystemMessage) {
        this(message, null, isSystemMessage);
    }
}
