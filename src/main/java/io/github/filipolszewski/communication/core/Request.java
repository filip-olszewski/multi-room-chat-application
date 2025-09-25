package io.github.filipolszewski.communication.core;

import java.io.Serializable;

public record Request<T extends Payload>(T payload) implements Serializable {
}
