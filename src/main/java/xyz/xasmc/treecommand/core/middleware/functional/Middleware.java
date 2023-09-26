package xyz.xasmc.treecommand.core.middleware.functional;

import xyz.xasmc.treecommand.core.middleware.Context;

@FunctionalInterface
public interface Middleware {
    void handle(Context context, NextMiddleware next);
}
