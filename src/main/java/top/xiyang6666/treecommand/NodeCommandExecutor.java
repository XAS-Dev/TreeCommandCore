package top.xiyang6666.treecommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface NodeCommandExecutor {
    public boolean apply(CommandSender commandSender, Command command, String s, String[] strings);
}
