package io.github.filipolszewski.client.ui.screens;

import io.github.filipolszewski.dto.RoomDTO;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.client.ui.components.RoomListing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomeScreen extends JPanel {
    public final static String HOME_SCREEN_KEY = "HOME";

    public final JButton joinButton;
    public final JButton createButton;
    public final JButton deleteButton;
    public final JButton refreshButton;

    public final JPanel roomListPanel;

    public HomeScreen() {
        setLayout(new BorderLayout());

        joinButton = new JButton("Join");
        createButton = new JButton("Create");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");

        JToolBar toolBar = new JToolBar();
        toolBar.add(joinButton);
        toolBar.add(createButton);
        toolBar.add(deleteButton);
        toolBar.add(refreshButton);

        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        roomListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(roomListPanel);

        add(scrollPane, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
    }

    public void addCreateButtonListener(ActionListener actionListener) {
        createButton.addActionListener(actionListener);
    }

    public void addDeleteButtonListener(ActionListener actionListener) {
        deleteButton.addActionListener(actionListener);
    }

    public void addJoinButtonListener(ActionListener actionListener) {
        joinButton.addActionListener(actionListener);
    }

    public void addRefreshButtonListener(ActionListener actionListener) {
        refreshButton.addActionListener(actionListener);
    }

    public void clearRoomListings() {
        roomListPanel.removeAll();
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }

    public void addRoomListing(RoomDTO room, ActionListener actionListener) {
        RoomListing roomListing = new RoomListing(room);

        roomListing.getJoinButton().addActionListener(actionListener);

        roomListPanel.add(Box.createVerticalStrut(5));
        roomListing.setMaximumSize(new Dimension(Integer.MAX_VALUE, roomListing.getPreferredSize().height));

        roomListPanel.add(roomListing);
    }
}
