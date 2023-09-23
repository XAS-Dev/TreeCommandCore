package xyz.xasmc.treecommand.node.argument;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.node.ExecutableNode;

/**
 * 用于在指令树中插入一个执行器
 * 默认为可结束节点
 * 可作为指令结束点的标志,并且为可执行节点,可以在指令结束处执行相应操作
 */
public class ExecuteNode extends ExecutableNode {
    protected boolean terminable = true;

    /**
     * 获取补全数组
     *
     * @param sender 指令发送者
     * @param args   参数列表
     * @return 补全数组
     */
    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        return null;
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
}
