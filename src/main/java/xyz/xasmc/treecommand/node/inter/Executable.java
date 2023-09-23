package xyz.xasmc.treecommand.node.inter;

import xyz.xasmc.treecommand.function.Executor;
import xyz.xasmc.treecommand.node.NodeInter;

public interface Executable extends NodeInter {
    Executor getExecutor();

    void setExecutor(Executor executor);
}
