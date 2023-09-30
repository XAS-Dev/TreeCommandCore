package xyz.xasmc.treecommand.core.middleware;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;
import xyz.xasmc.treecommand.core.state.State;

import java.util.Map;

public abstract class BaseContext {
    protected State state;

    public BaseContext(State state) {
        this.state = state;
    }

    // ===== State =====

    public State getState() {
        return this.state;
    }

    public RootNode getRootNode() {
        return this.state.rootNode;
    }

    public CommandSender getSender() {
        return this.state.sender;
    }

    public String getLabel() {
        return this.state.label;
    }

    public String[] getArgs() {
        return this.state.args;
    }

    public BaseNode getLastNode() {
        return this.state.lastNode;
    }

    public Map<String, Object> getStateMap() {
        return this.state.state;
    }

    public Object getValue(String name) {
        return this.state.state.get(name);
    }

    public <T> T getValue(String name, Class<T> type) {
        return type.cast(this.state.state.get(name));
    }
}
