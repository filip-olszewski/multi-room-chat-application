package io.github.filipolszewski.client;

import javax.swing.*;

public class ClientStarter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Client().init();
        });
    }
}
