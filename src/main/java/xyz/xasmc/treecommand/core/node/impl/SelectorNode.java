package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.marker.Parseable;
import xyz.xasmc.treecommand.core.util.Intersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectorNode extends BaseNode implements Parseable {
    private final String[] selectors = {"@p", "@r", "@a", "@e", "@s"};
    // ===== Parseable =====

    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        return Bukkit.selectEntities(sender, args[0]);
    }

    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        // TODO: @x[...] 补全
        List<String> result = new ArrayList<>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Entity entity = Intersection.getIntersectionWithEntity(player);
            if (entity != null) {
                result.add(entity.getUniqueId().toString());
            }
        }
        result.addAll(Arrays.asList(this.selectors));
        for (Player player : Bukkit.getOnlinePlayers()) {
            result.add(player.getName());
        }
        return result.toArray(new String[0]);
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        // TODO: @x[...]判断
        if (unprocessedArgs.length == 0) return -1;
        else return 1;
    }
}
