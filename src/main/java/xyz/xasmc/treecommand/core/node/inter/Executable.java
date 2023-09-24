package xyz.xasmc.treecommand.core.node.inter;

import xyz.xasmc.treecommand.core.function.Executor;
import xyz.xasmc.treecommand.core.node.NodeInter;

public interface Executable extends NodeInter {
    Executor getExecutor();

    void setExecutor(Executor executor);
}
