package io.github.filipolszewski.server.events;

import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Log
public class EventBus {

    private final Map<Class<? extends Event>, List<Consumer<? extends Event>>> eventListeners;

    public EventBus() {
        eventListeners = new HashMap<>();
    }

    public <T extends Event> void registerEvent(Class<T> type, Consumer<T> listener) {
        eventListeners.computeIfAbsent(type, _ -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void emit(T e) {
        List<Consumer<? extends Event>> consumers = eventListeners.get(e.getClass());

        if(consumers == null) {
            log.warning("Events for " + e.getClass() + " have not been registered");
            return;
        }

        consumers.forEach(consumer -> ((Consumer<T>) consumer).accept(e));
    }
}
