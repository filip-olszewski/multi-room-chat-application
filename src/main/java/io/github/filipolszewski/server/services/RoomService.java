package io.github.filipolszewski.server.services;

import io.github.filipolszewski.constants.status.room.*;
import io.github.filipolszewski.model.room.Room;
import io.github.filipolszewski.constants.RoomPrivacyPolicy;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomService {
    private final Map<String, Room> rooms;

    public RoomService() {
        rooms = new ConcurrentHashMap<>();
    }

    public CreateRoomStatus createRoom(String roomID, String adminID, int capacity, RoomPrivacyPolicy privacy) {
        return rooms.putIfAbsent(roomID, new Room(roomID, adminID, capacity, privacy)) == null
                ? CreateRoomStatus.SUCCESS
                : CreateRoomStatus.ALREADY_EXISTS;
    }

    public CreateRoomStatus createRoom(Room room) {
        return this.createRoom(room.getRoomID(), room.getAdminID(), room.getCapacity(), room.getPrivacy());
    }

    public synchronized CreateAndJoinRoomStatus createAndJoinRoom(
            String roomID, String adminID, int capacity, RoomPrivacyPolicy privacy) {

        if (rooms.containsKey(roomID)) {
            return CreateAndJoinRoomStatus.ALREADY_EXISTS;
        }

        Room room = new Room(roomID, adminID, capacity, privacy);
        rooms.put(roomID, room);

        return room.joinRoom(adminID)
                ? CreateAndJoinRoomStatus.SUCCESS
                : CreateAndJoinRoomStatus.JOIN_FAILED;
    }

    public RemoveRoomStatus removeRoom(String roomID, String userID) {
        Room room = rooms.get(roomID);

        if(room == null) return RemoveRoomStatus.ROOM_NOT_FOUND;

        synchronized (room) {
            if(!room.isAdmin(userID)) {
                return RemoveRoomStatus.NOT_ADMIN;
            }
            if(!room.isEmpty()) {
                return RemoveRoomStatus.NOT_EMPTY;
            }
            if(rooms.remove(roomID, room)) {
                return RemoveRoomStatus.SUCCESS;
            }
        }

        return RemoveRoomStatus.ROOM_NOT_FOUND;
    }

    public JoinRoomStatus joinRoom(String roomID, String userID) {
        Room room = rooms.get(roomID);

        if(room == null) return JoinRoomStatus.ROOM_NOT_FOUND;
        if(!room.joinRoom(userID)) return JoinRoomStatus.FULL;

        return JoinRoomStatus.SUCCESS;
    }

    public LeaveRoomStatus leaveRoom(String roomID, String userID) {
        Room room = rooms.get(roomID);

        if(room == null) return LeaveRoomStatus.ROOM_NOT_FOUND;
        if (!room.leaveRoom(userID)) return LeaveRoomStatus.USER_NOT_IN_ROOM;

        return LeaveRoomStatus.SUCCESS;
    }

    public Room getRoom(String roomID) {
        return rooms.get(roomID);
    }

    public Collection<Room> getAll() {
        return Collections.unmodifiableCollection(rooms.values());
    }

    public Collection<Room> getAll(RoomPrivacyPolicy privacy) {
        Collection<Room> publicRooms = rooms.values()
                .stream()
                .filter(room -> room.getPrivacy() == privacy)
                .toList();

        return publicRooms;
    }

}
