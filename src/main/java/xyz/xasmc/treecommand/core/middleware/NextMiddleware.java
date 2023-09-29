package xyz.xasmc.treecommand.core.middleware;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;

public class NextMiddleware {
    Context context;
    NextMiddleware nextMiddleware;
    Middleware middleware;

    public NextMiddleware() {

    }

    public NextMiddleware(Context context, Middleware middleware, NextMiddleware nextMiddleware) {
        this.context = context;
        this.nextMiddleware = nextMiddleware;
        this.middleware = middleware;
    }

    public void next() {
        if (this.middleware == null) return;
        this.middleware.handle(this.context, this.nextMiddleware);
    }
}
