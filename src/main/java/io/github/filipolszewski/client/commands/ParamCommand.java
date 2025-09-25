package io.github.filipolszewski.client.commands;

public interface ParamCommand<T> extends Command {
    void execute(T param);
}
