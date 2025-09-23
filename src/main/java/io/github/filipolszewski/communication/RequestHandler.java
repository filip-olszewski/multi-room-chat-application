package io.github.filipolszewski.communication;

import io.github.filipolszewski.server.ClientHandler;

public interface RequestHandler {
    Response<? extends Payload> handle(Request<? extends Payload> request, ClientHandler clientHandler);
}
