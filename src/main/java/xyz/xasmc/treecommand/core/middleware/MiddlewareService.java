package xyz.xasmc.treecommand.core.middleware;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiddlewareService {
    public static Context createContext(RootNode rootNode, String[] args, CommandSender sender, String label) {
        State state = new State();
        state.load(rootNode, args, sender, label);
        return new Context(state);
    }

    public static NextMiddleware loadContext(Context context) {
        List<BaseNode> executableList = new ArrayList<>(context.state.executableNodeList);
        Collections.reverse(executableList);
        NextMiddleware result = new NextMiddleware(); // 最内层的middleware
        for (BaseNode node : executableList) {
            result = new NextMiddleware(context, node, node.getMiddleware(), result);
        }
        result = new NextMiddleware(context, null, MiddlewareService::BaseMiddleware, result);
        return result;
    }

    protected static void BaseMiddleware(Context ctx, NextMiddleware next) {
        next.next();
        // 发送 MessageToSender
        ctx.getSender().spigot().sendMessage(ctx.getMessageToSender().toArray(new BaseComponent[0]));
        // 发送 MessageToBroadCast
        Bukkit.spigot().broadcast(ctx.getMessageToBroadcast().toArray(new BaseComponent[0]));
    }
}
