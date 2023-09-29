package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

import javax.annotation.Nullable;

public class SubCommandNode extends BaseNode implements Parseable, Executable {
    protected String label = null;
    protected boolean caseSensitive = true;

    // ===== init =====
    public SubCommandNode() {
    }

    public SubCommandNode(String label) {
        this.label = label;
    }

    public SubCommandNode(String label, boolean caseSensitive) {
        this.label = label;
        this.caseSensitive = caseSensitive;
    }

    // ===== Parseable =====

    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        return null;
    }

    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        return new String[]{this.label};
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        if (unprocessedArgs.length == 0) return -1;
        if (this.caseSensitive) return unprocessedArgs[0].equals(this.label) ? 1 : -1;
        else return unprocessedArgs[0].equalsIgnoreCase(this.label) ? 1 : -1;
    }

    // ===== custom =====

    /**
     * 设置是否区分大小写
     *
     * @param caseSensitive 是否区分大小写
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * 设置子指令标签
     *
     * @param label 标签
     */

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
