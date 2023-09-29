package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockNode extends BaseNode implements Parseable {
    // ===== Parseable =====
    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        String[] resultArray = {"~", "~", "~"};
        if (sender instanceof Player) { // 发送者是玩家
            Player player = (Player) sender;
            Block block = player.getTargetBlockExact(4); // 获取选中的方块坐标
            Location blockLocation = block == null ? null : block.getLocation();
            if (blockLocation != null) { // 玩家指针选中了一个方块
                resultArray[0] = String.format("%.0f", blockLocation.getX());
                resultArray[1] = String.format("%.0f", blockLocation.getY());
                resultArray[2] = String.format("%.0f", blockLocation.getZ());
            }
        }
        Pattern numberPattern = Pattern.compile("^(~)?([+-][0-9]+|[0-9]*)?$");
        for (int i = 0; i < Math.min(args.length, 3); i++) {
            if (args[i].isEmpty()) break;
            if (!numberPattern.matcher(args[i]).matches()) return null;
            resultArray[i] = args[i];
        }
        // 返回结果
        // eg.
        // ~
        // ~ ~
        // ~ ~ ~
        List<String> resultList = new ArrayList<>();
        String[] needfulArgs = Arrays.copyOfRange(resultArray, args.length - 1, resultArray.length);
        for (int i = 1; i <= needfulArgs.length; i++)
            resultList.add(String.join(" ", Arrays.copyOfRange(needfulArgs, 0, i)));
        return resultList.toArray(new String[0]);
    }

    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        if (unprocessedArgs.length == 0) return -1;
        Pattern numberPattern = Pattern.compile("^(~)?([+-][0-9]+|[0-9]*)?$");
        boolean result = true;
        for (int i = 0; i < Math.min(3, unprocessedArgs.length); i++) {
            result &= numberPattern.matcher(unprocessedArgs[i]).matches();
        }
        return result ? 3 : -1;
    }

    @Nullable
    @Override
    public Block parseArgument(CommandSender sender, String[] args) {
        Block result = null;
        Pattern numberPattern = Pattern.compile("^(~)?([+-][0-9]+|[0-9]*)?$");
        boolean[] relativeArray = new boolean[3];
        int[] valueArray = new int[3];
        boolean relative;
        int value;
        // 匹配坐标
        for (int i = 0; i < 3; i++) {
            Matcher Result = numberPattern.matcher(args[i]);
            if (Result.find()) {
                relative = Result.group(1) != null;
                value = Result.group(2) == null ? 0 : Integer.parseInt(Result.group(2));
            } else {
                return null;
            }
            relativeArray[i] = relative;
            valueArray[i] = value;
        }
        // 处理数值
        boolean xRelative = relativeArray[0];
        boolean yRelative = relativeArray[1];
        boolean zRelative = relativeArray[2];
        int xValue = valueArray[0];
        int yValue = valueArray[1];
        int zValue = valueArray[2];
        if (sender instanceof Entity) {
            Entity entity = (Entity) sender;
            int x = (xRelative ? (int) entity.getLocation().getX() : 0) + xValue;
            int y = (yRelative ? (int) entity.getLocation().getY() : 0) + yValue;
            int z = (zRelative ? (int) entity.getLocation().getZ() : 0) + zValue;
            // result = new Location(entity.getWorld(), x, y, z);
            result = entity.getWorld().getBlockAt(x, y, z);
        } else if (sender instanceof ConsoleCommandSender) {
            result = Bukkit.getWorld("world").getBlockAt(xValue, yValue, zValue);
        }
        return result;
    }
}
