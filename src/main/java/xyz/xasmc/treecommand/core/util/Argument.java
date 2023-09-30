package xyz.xasmc.treecommand.core.util;

import xyz.xasmc.treecommand.core.node.ArgumentType;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.impl.*;

public abstract class Argument {
    public static BaseNode getArgumentByType(ArgumentType type) {
        switch (type) {
            case BLOCK:
                return new BlockNode();
            case ENUM:
                return new EnumNode();
            case OFFLINE_PLAYER:
                return new OfflinePlayerNode();
            case PLAYER:
                return new PlayerNode();
            case LOCATION:
                return new LocationNode();
            case SELECTOR:
                return new SelectorNode();
        }
        return null;
    }
}
