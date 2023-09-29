package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.config.PlayerNodeConfig;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerNode extends BaseNode implements Parseable {
    protected PlayerNodeConfig config;

    // ===== Parseable =====
    @Override
    @Nullable
    public String[] getCompletion(CommandSender sender, String[] args) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        List<String> result = new ArrayList<>();
        for (Player player : players) {
            result.add(player.getName());
        }
        return result.toArray(new String[0]);
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        if (unprocessedArgs.length == 0) return -1;
        if (!this.config.isCheckPlayerName) return 1;
        Player player = Bukkit.getPlayerExact(unprocessedArgs[0]);// 这里不要求大小写
        if (player == null) return -1;
        return 1;
    }

    @Nullable
    @Override
    public Player parseArgument(CommandSender sender, String[] args) {
        return Bukkit.getPlayerExact(args[0]);
    }
}
