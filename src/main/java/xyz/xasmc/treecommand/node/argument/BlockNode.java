package xyz.xasmc.treecommand.node.argument;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.node.ParseableNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class BlockNode extends ParseableNode {
    /**
     * 获取补全数组
     *
     * @param sender 发送者
     * @param args   参数列表
     * @return 补全数组
     */
    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        int argc = 0;
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
        Pattern numberPattern = Pattern.compile("^(~?)((?:[+-][0-9]+|[0-9]*)?)$");
        for (int i = 0; i < args.length && i < 3; i++) {
            if (args[i].isEmpty()) break;
            if (!numberPattern.matcher(args[i]).matches()) return null;
            resultArray[i] = args[i];
            argc++;
        }
        // 返回结果
        // 返回从下往上(3-有效参数长度)层
        // eg.
        // ~
        // ~ ~
        // ~ ~ ~
        List<String> resultList = new ArrayList<>();
        for (int i = argc; i <= 3; i++)
            resultList.add(String.join(" ", Arrays.copyOfRange(resultArray, 0, i)));
        return resultList.toArray(new String[0]);
    }

    /**
     * 获取处理参数数量
     * 输入参数数组,返回要处理的参数数量
     *
     * @param unprocessedArgs 到该节点时还未处理的参数数组
     * @return 占用参数数量(可以超出参数数组长, - 1 则为错误)
     */
    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        return 0;
    }

    /**
     * 处理参数
     * 处理参数,返回处理后的结果
     *
     * @param sender 发送者
     * @param args   参数数组
     * @return 处理后结果
     */
    @Nullable
    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        return null;
    }
}
