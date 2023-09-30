package xyz.xasmc.treecommand.core.middleware.functional;

import xyz.xasmc.treecommand.core.middleware.BaseContext;
import xyz.xasmc.treecommand.core.middleware.NextMiddleware;

@FunctionalInterface
public interface Middleware {
    void handle(BaseContext ctx, NextMiddleware next);
}
