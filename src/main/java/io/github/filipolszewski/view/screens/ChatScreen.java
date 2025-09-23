package io.github.filipolszewski.view.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChatScreen extends JPanel {
    public final static String CHAT_SCREEN_KEY = "CHAT";

    private final JTextArea textArea;
    private final JTextField input;
    private final JButton sendButton;
    private final JButton leaveButton;

    public ChatScreen() {
        setLayout(new BorderLayout());

        textArea = new JTextArea(24, 32);
        textArea.setFocusable(false);
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));


        input = new JTextField(16);
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(input);
        bottomPanel.add(sendButton);


        leaveButton = new JButton("Leave");

        JToolBar toolbar = new JToolBar();
        toolbar.add(leaveButton);


        add(toolbar, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addLeaveButtonListener(ActionListener actionListener) {
        leaveButton.addActionListener(actionListener);
    }

    public void addSendButtonActionListener(ActionListener actionListener) {
        sendButton.addActionListener(actionListener);
    }
}
