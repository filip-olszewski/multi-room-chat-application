package io.github.filipolszewski.client.ui.components;

import io.github.filipolszewski.model.room.Room;
import lombok.Getter;

import javax.swing.*;

public class RoomListing extends JPanel {
    private final JLabel roomIdLabel;
    private final JLabel activeUsers;

    @Getter
    private final JButton joinButton;

    public RoomListing(Room room) {
        roomIdLabel = new JLabel(room.getRoomID());

        String activeUsersStr = Integer.toString(room.getActiveUsers().size());
        String maxUsersStr =  Integer.toString(room.getCapacity());
        activeUsers = new JLabel(activeUsersStr + " / " + maxUsersStr);

        joinButton = new JButton("Join room");

        add(roomIdLabel);
        add(activeUsers);
        add(joinButton);
    }
}
