package xyz.xasmc.treecommand.core.node.marker;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public interface Parseable {
    /**
     * 处理参数
     *
     * @param sender 指令发送者
     * @param args   参数列表
     * @return 处理后的参数
     */
    Object parseArgument(CommandSender sender, String[] args);

    /**
     * 获取补全数组
     *
     * @param sender 指令发送者
     * @param args   参数列表
     * @return 补全数组
     */
    @Nullable
    String[] getCompletion(CommandSender sender, String[] args);

    /**
     * 获取参数数量
     * 输入参数数组,返回将会处理的参数数量
     *
     * @param unprocessedArgs 未处理的参数数组
     * @return 占用参数数量
     */
    int getArgsQuantity(String[] unprocessedArgs);
}
