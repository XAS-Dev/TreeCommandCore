package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.config.EnumNodeConfig;
import xyz.xasmc.treecommand.core.node.marker.Parsable;

public class EnumNode extends BaseNode implements Parsable {
    protected EnumNodeConfig config = new EnumNodeConfig(new String[]{"not_specified_awa"});

    @Override
    public void initConfig() {
        this.config = (EnumNodeConfig) super.config;
    }

    // ===== Parsable =====

    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        for (String s : this.config.enmuList) {
            if (this.config.caseSensitive ? s.equals(args[0]) : s.equalsIgnoreCase(args[0])) {
                return s;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        return this.config.enmuList.toArray(new String[0]);
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        return unprocessedArgs.length == 0 ? -1 : 1;
    }
}
