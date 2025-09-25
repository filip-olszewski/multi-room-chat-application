package io.github.filipolszewski.server.services;

import io.github.filipolszewski.communication.RoomUpdateType;
import io.github.filipolszewski.server.Server;

public class RoomNotifierService {

    private final Server server;
    private final RoomService roomService;

    public RoomNotifierService(Server server, RoomService roomService) {
        this.server = server;
        this.roomService = roomService;
    }

    public void broadcastRoomUpdate(String roomID, RoomUpdateType type) {

    }
}
