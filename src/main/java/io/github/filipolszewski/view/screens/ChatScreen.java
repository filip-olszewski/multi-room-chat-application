package io.github.filipolszewski.view.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
        JScrollPane scrollPane = new JScrollPane(textArea);

        input = new JTextField(16);
        input.requestFocus();
        sendButton = new JButton("Send");

        // Set the send button to be a default button of the frame (react to Enter Key)
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                JRootPane rootPane = SwingUtilities.getRootPane(ChatScreen.this);
                if (rootPane != null) {
                    rootPane.setDefaultButton(sendButton);
                }
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(input);
        bottomPanel.add(sendButton);

        leaveButton = new JButton("Leave");

        JToolBar toolbar = new JToolBar();
        toolbar.add(leaveButton);

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addLeaveButtonListener(ActionListener actionListener) {
        leaveButton.addActionListener(actionListener);
    }

    public void addSendButtonActionListener(ActionListener actionListener) {
        sendButton.addActionListener(actionListener);
    }

    public String getInputText() {
        return input.getText();
    }

    public void appendUserMessage(String message, String sender) {
        String messageToAppend = "(" + sender + "): " + message;
        textArea.append(messageToAppend + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void appendSystemMessage(String message) {
        textArea.append(message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void clearInput() {
        input.setText("");
        input.requestFocus();
    }

    public void clearChat() {
        textArea.setText("");
    }
}
