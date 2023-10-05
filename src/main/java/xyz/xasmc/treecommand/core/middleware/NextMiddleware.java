package xyz.xasmc.treecommand.core.middleware;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;
import xyz.xasmc.treecommand.core.node.BaseNode;

public class NextMiddleware {
    Context context;
    BaseNode node;
    NextMiddleware nextMiddleware;
    Middleware middleware;

    public NextMiddleware() {
    }

    public NextMiddleware(Context context, BaseNode node, Middleware middleware, NextMiddleware nextMiddleware) {
        this.context = context;
        this.node = node;
        this.nextMiddleware = nextMiddleware;
        this.middleware = middleware;
    }

    public void next() {
        if (this.middleware == null) return;
        this.context.nowNode = this.node;
        this.middleware.handle(this.context, this.nextMiddleware);
    }
}
