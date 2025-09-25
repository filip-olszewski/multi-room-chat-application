package io.github.filipolszewski.server.services;

import io.github.filipolszewski.model.user.User;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log
public class UserService {
    private final Map<String, User> users;

    public UserService() {
        users = new ConcurrentHashMap<>();
    }

    public boolean addUser(User user) {
        return users.putIfAbsent(user.getUserID(), user) == null;
    }

    public User getOrAddUser(String userID) {
        return users.computeIfAbsent(userID, User::new);
    }

    public boolean removeUser(String userID) {
        return users.remove(userID) != null;
    }

    public User getUser(String userID) {
        return users.get(userID);
    }

    public Collection<User> getAll() {
        return Collections.unmodifiableCollection(users.values());
    }
}
