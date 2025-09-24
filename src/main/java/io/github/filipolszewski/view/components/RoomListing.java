package io.github.filipolszewski.view.components;

import lombok.Getter;

import javax.swing.*;

public class RoomListing extends JPanel {
    private final JLabel roomIdLabel;

    @Getter
    private final JButton joinButton;

    public RoomListing(String roomID) {
        roomIdLabel = new JLabel(roomID);
        joinButton = new JButton("Join room");

        add(roomIdLabel);
        add(joinButton);
    }
}
