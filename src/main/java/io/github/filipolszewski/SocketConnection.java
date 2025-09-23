package io.github.filipolszewski;

import io.github.filipolszewski.connection.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection implements Connection {

    private final Object outputLock = new Object();
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public String recieve() throws IOException {
        return input.readLine();
    }

    @Override
    public void send(String packet) {
        synchronized (outputLock) {
           output.println(packet);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        output.close();
        input.close();
    }
}
