package io.github.filipolszewski.connection;

import java.io.*;
import java.net.Socket;

public class SocketConnection<S extends Serializable, R extends Serializable> implements Connection<S, R> {

    private final Object outputLock = new Object();
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    @SuppressWarnings("unchecked")
    @Override
    public R recieve() throws IOException, ClassNotFoundException {
        return (R) input.readObject();
    }

    @Override
    public void send(S packet) throws IOException {
        synchronized (outputLock) {
           output.writeObject(packet);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        output.close();
        input.close();
    }
}
