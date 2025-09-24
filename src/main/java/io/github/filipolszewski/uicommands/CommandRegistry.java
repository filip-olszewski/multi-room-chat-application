package io.github.filipolszewski.uicommands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<Class<?>, Command> commands;

    public CommandRegistry() {
        commands = new HashMap<>();
    }

    public <T> void register(Class<? extends Command> type, Command paramCommand) {
        commands.put(type, paramCommand);
    }

    @SuppressWarnings("unchecked")
    public <T> ParamCommand<T> getParam(Class<? extends ParamCommand<T>> type) {
        return (ParamCommand<T>) commands.get(type);
    }

    public NoArgsCommand getNoArgs(Class<? extends NoArgsCommand> type) {
        return (NoArgsCommand) commands.get(type);
    }
}
