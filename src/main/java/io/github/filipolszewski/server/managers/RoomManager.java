package io.github.filipolszewski.server.managers;

import io.github.filipolszewski.model.room.Room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final Map<String, Room> rooms;

    public RoomManager() {
        rooms = new ConcurrentHashMap<>();
    }

    /**
     * Adds a new room to the room list
     * @param room  A new room object
     * @return      True if successfully added, false otherwise
     */
    public boolean addRoom(Room room) {
        return rooms.putIfAbsent(room.getRoomID(), room) == null;
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
}
