package io.github.filipolszewski.server;

import io.github.filipolszewski.server.events.EventBus;
import io.github.filipolszewski.server.services.RoomService;
import io.github.filipolszewski.server.services.UserService;

public record ServerContext(RoomService roomService,
                            UserService userService,
                            EventBus eventBus) {}
