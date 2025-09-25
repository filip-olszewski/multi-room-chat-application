package io.github.filipolszewski.view;

import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.constants.WindowConfig;
import io.github.filipolszewski.view.components.CreateRoomDialog;
import io.github.filipolszewski.view.screens.ChatScreen;
import io.github.filipolszewski.view.screens.HomeScreen;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class AppWindow {
    private final JFrame frame;
    private final JPanel mainPanel;
    @Getter private final HomeScreen homeScreen;
    @Getter private final ChatScreen chatScreen;

    public AppWindow() {
        frame = new JFrame(WindowConfig.TITLE);
        frame.setSize(WindowConfig.WIDTH, WindowConfig.HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        homeScreen = new HomeScreen();
        chatScreen = new ChatScreen();

        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(homeScreen, HomeScreen.HOME_SCREEN_KEY);
        mainPanel.add(chatScreen, ChatScreen.CHAT_SCREEN_KEY);

        frame.add(mainPanel);
        showHomeScreen();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    public void showChatScreen() {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, ChatScreen.CHAT_SCREEN_KEY);
    }

    public void showHomeScreen() {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, HomeScreen.HOME_SCREEN_KEY);
    }

    public CreateRoomPayload promptCreateRoom(String message) {
        return new CreateRoomDialog().showDialog(mainPanel, message);
    }

    public String promptInputDialog(String message) {
        return JOptionPane.showInputDialog(mainPanel, message);
    }

    public int displayConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(mainPanel, message, "Are you sure?", JOptionPane.OK_CANCEL_OPTION);
    }

    public void displaySuccessDialog(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Success!", JOptionPane.PLAIN_MESSAGE);
    }

    public void displayErrorDialog(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
