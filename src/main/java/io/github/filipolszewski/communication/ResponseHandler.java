package io.github.filipolszewski.communication;

import io.github.filipolszewski.client.Client;

public interface ResponseHandler {
    void handle(Response<? extends Payload> response, Client client);
}
