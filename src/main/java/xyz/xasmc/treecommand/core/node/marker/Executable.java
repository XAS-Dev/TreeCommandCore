package xyz.xasmc.treecommand.core.node.marker;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;

public interface Executable {
    Middleware getMiddleware();

    void setMiddleware(Middleware middleware);
}
