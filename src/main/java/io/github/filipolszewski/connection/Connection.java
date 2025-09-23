package io.github.filipolszewski.connection;

import java.io.IOException;

public interface Connection<S, R> extends AutoCloseable {
    R recieve() throws IOException, ClassNotFoundException;
    void send(S packet) throws IOException;
    @Override void close() throws IOException;
}
