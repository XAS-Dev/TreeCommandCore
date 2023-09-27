package xyz.xasmc.treecommand.core.middleware.functional;

import xyz.xasmc.treecommand.core.middleware.Context;
import xyz.xasmc.treecommand.core.middleware.NextMiddleware;

@FunctionalInterface
public interface Middleware {
    void handle(Context context, NextMiddleware next);
}
