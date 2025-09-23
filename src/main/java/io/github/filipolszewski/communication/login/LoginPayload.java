package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.communication.Payload;

public record LoginPayload(String userID) implements Payload {
}
