package xyz.xasmc.treecommand.core.node.type;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.inter.Parseable;
import xyz.xasmc.treecommand.core.util.Intersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionNode extends BaseNode implements Parseable {
    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        String[] resultArray = {"~", "~", "~"};
        if (sender instanceof Player) { // 发送者是玩家
            Player player = (Player) sender;
            Vector position = Intersection.getIntersection(player);
            if (position != null) { // 玩家指针与方块有交点
                resultArray[0] = String.format("%.2f", position.getX());
                resultArray[1] = String.format("%.2f", position.getY());
                resultArray[2] = String.format("%.2f", position.getZ());
            }
        }
        Pattern numberPattern = Pattern.compile("^(~)?([-+]?(?:\\d*\\.\\d+|\\d+\\.\\d*|\\d+))?$");
        for (int i = 0; i < Math.min(args.length, 3); i++) {
            // 匹配参数是否合法
            if (args[i].isEmpty()) break;
            if (!numberPattern.matcher(args[i]).matches()) return null; // 不匹配,返回null
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
        Pattern numberPattern = Pattern.compile("^(~)?([-+]?(?:\\d*\\.\\d+|\\d+\\.\\d*|\\d+))?$");
        boolean result = true;
        for (int i = 0; i < 3 && i < unprocessedArgs.length; i++) {
            result &= numberPattern.matcher(unprocessedArgs[i]).matches();
        }
        return result ? 3 : -1;
    }

    @Nullable
    @Override
    public Location parseArgument(CommandSender sender, String[] args) {
        Location result = null;
        Pattern numberPattern = Pattern.compile("^(~)?([-+]?(?:\\d*\\.\\d+|\\d+\\.\\d*|\\d+))?$");
        boolean[] relativeArray = new boolean[3];
        double[] valueArray = new double[3];
        boolean relative;
        double value;
        // 匹配坐标
        for (int i = 0; i < 3; i++) {
            Matcher Result = numberPattern.matcher(args[i]);
            if (Result.find()) {
                relative = Result.group(1) != null;
                value = Result.group(2) == null ? 0d : Double.parseDouble(Result.group(2));
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
        double xValue = valueArray[0];
        double yValue = valueArray[1];
        double zValue = valueArray[2];
        if (sender instanceof Entity) {
            Entity entity = (Entity) sender;
            double x = (xRelative ? entity.getLocation().getX() : 0d) + xValue;
            double y = (yRelative ? entity.getLocation().getY() : 0d) + yValue;
            double z = (zRelative ? entity.getLocation().getZ() : 0d) + zValue;
            result = new Location(entity.getWorld(), x, y, z);
        } else if (sender instanceof ConsoleCommandSender) {
            result = new Location(Bukkit.getWorld("world"), xValue, yValue, zValue);
        }
        return result;
    }
}
