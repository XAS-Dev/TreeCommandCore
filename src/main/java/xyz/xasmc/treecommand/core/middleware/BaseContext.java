package xyz.xasmc.treecommand.core.middleware;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;

import java.util.List;
import java.util.Map;

public abstract class BaseContext {
    protected State state;

    public BaseContext(State state) {
        this.state = state;
    }

    // ===== State attribute =====

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

    @Nullable
    public StateException getException() {
        return this.state.exception;
    }

    public int getProcessedArgsCount() {
        return this.state.processedArgsCount;
    }

    public BaseNode getLastNode() {
        return this.state.lastNode;
    }

    public Map<String, Object> getStateMap() {
        return this.state.state;
    }

    // ===== State method =====

    public boolean isSuccess() {
        return this.state.isSuccess();
    }

    // ===== state =====

    public Object get(String key) {
        return this.state.state.get(key);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(this.state.state.get(key));
    }

    // ===== ArgumentNode =====

    public Block getBlock(String key) {
        return (Block) this.get(key);
    }

    public String getEnum(String key) {
        return (String) this.get(key);
    }

    public Location getLocation(String key) {
        return (Location) this.get(key);
    }

    public OfflinePlayer getOfflinePlayer(String key) {
        return (OfflinePlayer) this.get(key);
    }

    public Player getPlayer(String key) {
        return (Player) this.get(key);
    }

    public List<Entity> getSelector(String key) {
        return (List<Entity>) this.get(key);
    }
}
