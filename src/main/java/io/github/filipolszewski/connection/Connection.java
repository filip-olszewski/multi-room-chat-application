package io.github.filipolszewski.connection;

import java.io.IOException;

public interface Connection extends AutoCloseable {
    String recieve() throws IOException;
    void send(String packet);
    @Override void close() throws IOException;
}
