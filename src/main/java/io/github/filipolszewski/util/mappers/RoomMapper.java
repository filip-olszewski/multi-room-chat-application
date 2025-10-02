package io.github.filipolszewski.util.mappers;

import io.github.filipolszewski.dto.RoomDTO;
import io.github.filipolszewski.model.room.Room;

import java.util.Set;

public class RoomMapper {
    public static RoomDTO toDTO(Room model) {
        synchronized (model) {
            return new RoomDTO(
                model.getRoomID(),
                model.getAdminID(),
                model.getCapacity(),
                model.getPrivacy(),
                Set.copyOf(model.getActiveUsers())
            );
        }
    }

    public static Room toModel(RoomDTO dto) {
        return new Room(
                dto.roomID(),
                dto.adminID(),
                dto.capacity(),
                dto.privacy()
        );
    }
}
