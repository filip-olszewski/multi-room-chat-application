package io.github.filipolszewski.communication.message;

import io.github.filipolszewski.communication.Payload;

public record MessagePayload(String message, String senderID) implements Payload {
}
