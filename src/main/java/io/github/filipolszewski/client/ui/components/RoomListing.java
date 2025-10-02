package io.github.filipolszewski.client.ui.components;

import io.github.filipolszewski.dto.RoomDTO;
import io.github.filipolszewski.model.room.Room;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoomListing extends JPanel {
    private final JLabel roomIdLabel;
    private final JLabel activeUsers;

    @Getter
    private final JButton joinButton;

    public RoomListing(RoomDTO room) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 10, 5, 5));

        roomIdLabel = new JLabel(room.roomID());

        String activeUsersStr = Integer.toString(room.activeUsers().size());
        String maxUsersStr =  Integer.toString(room.capacity());
        activeUsers = new JLabel(activeUsersStr + " / " + maxUsersStr);

        joinButton = new JButton("Join room");

        JPanel rightSideGroup = new JPanel();
        rightSideGroup.setOpaque(false);
        rightSideGroup.add(activeUsers);
        rightSideGroup.add(Box.createHorizontalStrut(10));
        rightSideGroup.add(joinButton);

        add(roomIdLabel, BorderLayout.WEST);
        add(rightSideGroup, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRoundRect(0, 0, getSize().width, getSize().height, 10, 10);
    }
}
