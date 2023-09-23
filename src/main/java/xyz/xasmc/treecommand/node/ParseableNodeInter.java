package xyz.xasmc.treecommand.node;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public interface ParseableNodeInter {
    /**
     * 处理参数
     * 处理参数,返回处理后的结果
     *
     * @param sender 发送者
     * @param args   参数数组
     * @return 处理后结果
     */
    @Nullable
    Object parseArgument(CommandSender sender, String[] args);
}
