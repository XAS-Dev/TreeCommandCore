package xyz.xasmc.treecommand.core.node.marker;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;
import xyz.xasmc.treecommand.core.node.NodeInter;


public interface Executable extends NodeInter {
    Middleware getMiddleware();

    void setMiddleware(Middleware middleware);
}
