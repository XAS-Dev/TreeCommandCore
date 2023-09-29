package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.config.SubCommandNodeConfig;
import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

import javax.annotation.Nullable;

public class SubCommandNode extends BaseNode implements Parseable, Executable {
    protected SubCommandNodeConfig config = new SubCommandNodeConfig();

    @Override
    public void initConfig() {
        this.config = (SubCommandNodeConfig) super.config;
    }

    // ===== Parseable =====

    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        return null;
    }

    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        return new String[]{this.config.label};
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        if (unprocessedArgs.length == 0) return -1;
        if (this.config.caseSensitive) return unprocessedArgs[0].equals(this.config.label) ? 1 : -1;
        else return unprocessedArgs[0].equalsIgnoreCase(this.config.label) ? 1 : -1;
    }
}
