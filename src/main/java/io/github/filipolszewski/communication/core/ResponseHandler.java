package io.github.filipolszewski.communication.core;

import io.github.filipolszewski.client.Client;

public interface ResponseHandler {
    void handle(Response<? extends Payload> response, Client client);
}
