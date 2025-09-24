package io.github.filipolszewski.view.screens;

import io.github.filipolszewski.view.components.RoomListing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
        BoxLayout boxLayout = new BoxLayout(roomListPanel, BoxLayout.Y_AXIS);
        roomListPanel.setLayout(boxLayout);

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

    public void addRoomListing(String roomID, ActionListener actionListener) {
        RoomListing roomListing = new RoomListing(roomID);

        roomListing.getJoinButton().addActionListener(actionListener);

        roomListPanel.add(roomListing);
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }
}
