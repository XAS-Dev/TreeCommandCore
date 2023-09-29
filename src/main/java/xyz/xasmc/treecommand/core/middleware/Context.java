package xyz.xasmc.treecommand.core.middleware;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;
import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.state.State;

import java.util.List;
import java.util.Map;

public class Context {
    protected State state;

    public Context(State state) {
        this.state = state;
    }

    // ===== State =====

    public State getState() {
        return this.state;
    }

    public RootNode getRootNode() {
        return this.state.getRootNode();
    }

    public CommandSender getSender() {
        return this.state.getSender();
    }

    public String getLabel() {
        return this.state.getLabel();
    }

    public String[] getArgs() {
        return this.state.getArgs();
    }

    public List<Executable> getExecutableNodes() {
        return this.state.getExecutableNodes();
    }

    public BaseNode getLastNode() {
        return this.state.getLastNode();
    }

    /**
     * 获取状态值
     *
     * @param name 键
     * @return 值
     */
    public Object getValue(String name) {
        return this.state.getValue(name);
    }

    /**
     * 获取状态值
     *
     * @param name 键
     * @return 值
     */
    public <T> T getValue(String name, Class<T> type) {
        return this.state.getValue(name, type);
    }

    /**
     * 获取状态Map
     *
     * @return 状态Map
     */
    public Map<String, Object> getStateMap() {
        return this.state.getStateMap();
    }

}
