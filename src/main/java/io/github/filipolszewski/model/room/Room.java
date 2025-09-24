package io.github.filipolszewski.model.room;

import io.github.filipolszewski.constants.RoomConfig;
import io.github.filipolszewski.server.managers.UserManager;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
public class Room {
    private final String roomID;
    private final String adminID;

    private volatile int capacity;
    private volatile RoomPrivacyPolicy privacy;

    private final Set<String> activeUsers;

    public Room(String roomID, String adminID, int capacity, RoomPrivacyPolicy privacy) {
        this.roomID = roomID;
        this.adminID = adminID;
        this.capacity = capacity;
        this.privacy = privacy;
        this.activeUsers = new HashSet<>();
    }
    public Room(String roomID, String adminUsername) {
        this(roomID, adminUsername, RoomConfig.DEFAULT_ROOM_CAPACITY, RoomConfig.DEFAULT_PRIVACY_POLICY);
    }

    public boolean isAdmin(String userID) {
        return userID.equals(adminID);
    }

    public synchronized boolean isEmpty() {
        return activeUsers.isEmpty();
    }

    public synchronized boolean changeCapacity(int newCapacity) {
        if(newCapacity < activeUsers.size()) {
            return false;
        }
        capacity = newCapacity;
        return true;
    }

    public synchronized void changePrivacyPolicy(RoomPrivacyPolicy newPrivacy) {
        privacy = newPrivacy;
    }

    /**
     * Joins the user to the room. If the current user count is equal
     * or more than the room's capacity then return false
     * @param userID    User set to join
     * @return          True if user was able to join, False otherwise
     */
    public synchronized boolean joinRoom(String userID) {
        if(activeUsers.size() >= capacity) {
            return false;
        }

        return activeUsers.add(userID);
    }

    public synchronized void leaveRoom(String userID) {
        activeUsers.remove(userID);
    }
}
