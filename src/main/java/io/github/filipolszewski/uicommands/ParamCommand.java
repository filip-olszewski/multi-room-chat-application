package io.github.filipolszewski.uicommands;

public interface ParamCommand<T> extends Command {
    void execute(T param);
}
