package io.github.filipolszewski.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class User {
    private final String userID;

    @Getter @Setter
    private String currentRoomID;
}
