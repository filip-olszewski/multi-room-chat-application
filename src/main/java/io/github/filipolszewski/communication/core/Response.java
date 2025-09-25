package io.github.filipolszewski.communication.core;

import java.io.Serializable;

public record Response<T extends Payload>(boolean success, String message, T payload) implements Serializable {
    public Response(String message, T payload) {
        this(true, message, payload);
    }
}
