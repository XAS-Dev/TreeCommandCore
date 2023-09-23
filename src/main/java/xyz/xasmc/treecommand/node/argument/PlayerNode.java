package xyz.xasmc.treecommand.node.argument;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.xasmc.treecommand.node.ParseableNode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerNode extends ParseableNode {
    boolean checkPlayerName = false;

    public PlayerNode() {
    }

    public PlayerNode(boolean checkPlayerName) {
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
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        List<String> result = new ArrayList<>();
        for (Player player : players) {
            result.add(player.getName());
        }
        return result.toArray(new String[0]);
    }

    /**
     * 获取改节点占用的指令参数数量
     *
     * @param unprocessedArgs 到该节点时还未处理的参数数组
     * @return 占用参数数量(可以超出参数数组长, - 1 则为错误)
     */
    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        if (unprocessedArgs.length == 0) return -1;
        if (!checkPlayerName) return 1;
        Player player = Bukkit.getPlayerExact(unprocessedArgs[0]);// 这里不要求大小写
        if (player == null) return -1;
        return 1;
    }

    /**
     * 获取参数处理后的结果
     *
     * @param args 参数列表
     * @return 处理后结果
     */
    @Nullable
    @Override
    public Player parseArgument(CommandSender sender, String[] args) {
        return Bukkit.getPlayerExact(args[0]);
    }

    // ===== custom ====

    public void setCheckPlayerName(boolean checkPlayerName) {
        this.checkPlayerName = checkPlayerName;
    }
}
