package io.github.filipolszewski.view.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomeScreen extends JPanel {
    public final static String HOME_SCREEN_KEY = "HOME";

    public final JButton joinButton;
    public final JButton createButton;
    public final JButton deleteButton;

    public HomeScreen() {
        setLayout(new BorderLayout());

        joinButton = new JButton("Join");
        createButton = new JButton("Create");
        deleteButton = new JButton("Delete");

        JToolBar toolBar = new JToolBar();
        toolBar.add(joinButton);
        toolBar.add(createButton);
        toolBar.add(deleteButton);

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
}
