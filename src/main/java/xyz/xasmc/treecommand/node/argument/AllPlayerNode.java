package xyz.xasmc.treecommand.node.argument;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.xasmc.treecommand.node.ParseableNode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllPlayerNode extends ParseableNode {
    protected boolean checkPlayerName = false;

    public AllPlayerNode() {
    }

    public AllPlayerNode(boolean checkPlayerName) {
        this.checkPlayerName = checkPlayerName;
    }

    /**
     * 获取补全数组
     *
     * @param args 参数列表
     * @return 补全数组
     */
    @Override
    @Nullable
    public String[] getCompletion(CommandSender sender, String[] args) {
        List<String> result = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers())// 获取所有离线玩家
            result.add(player.getName());
        for (Player player : Bukkit.getOnlinePlayers())// 获取所有在线玩家
            result.add(player.getName());
        return result.toArray(new String[0]);
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
        if (unprocessedArgs.length == 0) return -1;
        if (!checkPlayerName) return 1;
        List<String> playerNameList = new ArrayList<>();// 所有玩家名列表
        for (OfflinePlayer player : Bukkit.getOfflinePlayers())// 获取所有离线玩家
            playerNameList.add(player.getName());
        for (Player player : Bukkit.getOnlinePlayers())// 获取所有在线玩家
            playerNameList.add(player.getName());
        for (String playerName : playerNameList)
            if (playerName.equalsIgnoreCase(unprocessedArgs[0])) return 1;// 玩家名在所有玩家列表中
        return -1;// 没有匹配的
    }

    /**
     * 处理参数
     * 处理参数,返回处理后的结果
     *
     * @param args 参数数组
     * @return 处理后结果
     */

    @Nullable
    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        return null;
    }

    // ===== custom =====

    public void setCheckPlayerName(boolean checkPlayerName) {
        this.checkPlayerName = checkPlayerName;
    }
}
