package io.github.filipolszewski.communication;

public interface ResponseHandler {
    void handle(Response<? extends Payload> response);
}
