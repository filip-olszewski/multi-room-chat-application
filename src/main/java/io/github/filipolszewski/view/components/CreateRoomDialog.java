package io.github.filipolszewski.view.components;

import io.github.filipolszewski.communication.createroom.CreateRoomPayload;
import io.github.filipolszewski.constants.RoomConfig;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import javax.swing.*;
import java.awt.*;

public class CreateRoomDialog {
    private final JTextField roomIdField;
    private final JSpinner capacitySpinner;
    private final JComboBox<RoomPrivacyPolicy> privacyBox;

    public CreateRoomDialog() {

        roomIdField = new JTextField(24);

        var model = new SpinnerNumberModel(RoomConfig.DEFAULT_ROOM_CAPACITY,
                RoomConfig.MIN_ROOM_CAPACITY,
                RoomConfig.MAX_ROOM_CAPACITY,
                1);
        capacitySpinner = new JSpinner(model);

        privacyBox = new JComboBox<>(RoomPrivacyPolicy.values());
        privacyBox.setSelectedItem(RoomConfig.DEFAULT_PRIVACY_POLICY);
    }

    public CreateRoomPayload showDialog(JComponent parent, String message) {

        // Create the UI
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Room ID:"));
        panel.add(roomIdField);

        panel.add(new JLabel("Capacity:"));
        panel.add(capacitySpinner);

        panel.add(new JLabel("Privacy:"));
        panel.add(privacyBox);

        // Prompt user
        int result = prompt(parent, panel, message);

        // Return payload if OK
        if (result == JOptionPane.OK_OPTION) {
            return new CreateRoomPayload(
                    roomIdField.getText().trim(),
                    (Integer) capacitySpinner.getValue(),
                    (RoomPrivacyPolicy) privacyBox.getSelectedItem()
            );
        } else {
            return null;
        }
    }

    private int prompt(JComponent parent, JPanel panel, String message) {
        return JOptionPane.showConfirmDialog(
                parent,
                panel,
                message,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
    }
}
