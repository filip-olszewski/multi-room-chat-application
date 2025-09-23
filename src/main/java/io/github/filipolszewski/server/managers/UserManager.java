package io.github.filipolszewski.server.managers;

import io.github.filipolszewski.model.user.User;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Log
public class UserManager {
    private final Map<String, User> users;

    public UserManager() {
        users = new ConcurrentHashMap<>();
    }

    public boolean addUser(User user) {
        return users.putIfAbsent(user.getUserID(), user) == null;
    }

    public boolean removeUser(String userID) {
        return users.remove(userID) != null;
    }

    public Collection<User> getAll() {
        return Collections.unmodifiableCollection(users.values());
    }
}
