package xyz.xasmc.treecommand.node;

import xyz.xasmc.treecommand.function.Executor;

public abstract class ExecutableNode extends BaseNode {
    protected Executor executor = (state, next) -> next.apply();

    public Executor getExecutor() {
        return this.executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
