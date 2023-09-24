package xyz.xasmc.treecommand.core.function;

import xyz.xasmc.treecommand.core.state.State;

@FunctionalInterface
public interface Executor {
    boolean apply(State state, Next next);
}