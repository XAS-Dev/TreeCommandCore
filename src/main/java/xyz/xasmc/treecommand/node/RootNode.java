package xyz.xasmc.treecommand.node;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public class RootNode extends ExecutableNode {
    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        return -1;
    }
}
