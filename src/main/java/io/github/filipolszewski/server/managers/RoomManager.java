package io.github.filipolszewski.server.managers;

import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.model.room.RoomPrivacyPolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final Map<String, Room> rooms;

    public RoomManager() {
        rooms = new ConcurrentHashMap<>();
    }

    public boolean createRoom(String roomID, String adminID, int capacity, RoomPrivacyPolicy privacy) {
        return rooms.putIfAbsent(roomID, new Room(roomID, adminID, capacity, privacy)) == null;
    }

    public synchronized boolean createAndJoinRoom(String roomID, String adminID, int capacity, RoomPrivacyPolicy privacy) {
        Room room = new Room(roomID, adminID, capacity, privacy);

        if(rooms.containsKey(roomID)) return false;

        rooms.put(roomID, room);
        return room.joinRoom(adminID);
    }

    /**
     * Removes a room from the room list
     * @param roomID    ID of the room to be deleted
     * @return          True if successfully removed, false otherwise
     */
    public boolean removeRoom(String roomID, String userID) {
        Room room = rooms.get(roomID);

        if(room == null) return false;

        synchronized (room) {
            if(room.isAdmin(userID) && room.isEmpty()) {
                return rooms.remove(roomID, room);
            }
        }

        return false;
    }

    public boolean joinRoom(String roomID, String userID) {
        Room room = rooms.get(roomID);
        if(room != null) {
            return room.joinRoom(userID);
        }
        return false;
    }

    public void leaveRoom(String roomID, String userID) {
        Room room = rooms.get(roomID);
        if(room != null) {
            room.leaveRoom(userID);
        }
    }

    public Room getRoom(String roomID) {
        return rooms.get(roomID);
    }

}
