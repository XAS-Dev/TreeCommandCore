package xyz.xasmc.treecommand.core.node.marker;

import xyz.xasmc.treecommand.core.function.Executor;

public abstract class BaseExecutableImpl {
    // 不 implements Executable 因为BaseNode会继承该类导致所有节点均为 Executable
    protected Executor executor = (state, next) -> next.apply();

    public Executor getExecutor() {
        if (!(this instanceof Executable)) return null;
        return this.executor;
    }

    public void setExecutor(Executor executor) {
        if (!(this instanceof Executable)) return;
        this.executor = executor;
    }
}
