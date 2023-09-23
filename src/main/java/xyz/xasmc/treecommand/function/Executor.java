package xyz.xasmc.treecommand.function;

import xyz.xasmc.treecommand.state.State;

@FunctionalInterface
public interface Executor {
    boolean apply(State state, Next next);
}