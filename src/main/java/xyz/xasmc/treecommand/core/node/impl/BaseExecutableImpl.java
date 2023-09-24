package xyz.xasmc.treecommand.core.node.impl;

import xyz.xasmc.treecommand.core.node.inter.Executable;
import xyz.xasmc.treecommand.core.function.Executor;

public abstract class BaseExecutableImpl {
    protected Executor executor = (state, next) -> {
        boolean applied = next.apply();
        return applied;
    };

    public Executor getExecutor() {
        if (!(this instanceof Executable)) return null;
        return this.executor;
    }

    public void setExecutor(Executor executor) {
        if (!(this instanceof Executable)) return;
        this.executor = executor;
    }
}
