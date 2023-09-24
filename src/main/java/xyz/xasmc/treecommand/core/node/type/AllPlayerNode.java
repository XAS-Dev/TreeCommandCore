package xyz.xasmc.treecommand.core.node.type;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.inter.Parseable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllPlayerNode extends BaseNode implements Parseable {
    protected boolean checkPlayerName = false;

    // ===== init =====
    public AllPlayerNode() {
    }

    public AllPlayerNode(boolean checkPlayerName) {
        this.checkPlayerName = checkPlayerName;
    }

    // ===== Parseable =====

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
