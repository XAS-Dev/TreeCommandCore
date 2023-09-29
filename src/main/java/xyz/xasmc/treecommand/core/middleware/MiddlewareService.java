package xyz.xasmc.treecommand.core.middleware;

import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.state.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiddlewareService {

    public static NextMiddleware loadState(State state) {
        Context context = new Context(state);
        List<Executable> executableList = new ArrayList<>(state.getExecutableNodes());
        Collections.reverse(executableList);
        NextMiddleware result = new NextMiddleware(); // 最内层的middleware
        for (Executable node : executableList) {
            result = new NextMiddleware(context, node.getMiddleware(), result);
        }
        result = new NextMiddleware(context, MiddlewareService::BaseMiddleware, result);
        return result;
    }

    public static void BaseMiddleware(Context ctx, NextMiddleware next) {
        next.next();
    }
}
