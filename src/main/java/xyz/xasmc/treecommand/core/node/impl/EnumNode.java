package xyz.xasmc.treecommand.core.node.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.config.EnumNodeConfig;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

public class EnumNode extends BaseNode implements Parseable {
    protected EnumNodeConfig config;

    @Override
    public void initConfig() {
        this.config = (EnumNodeConfig) super.config;
    }

    // ===== Parseable =====

    /**
     * 处理参数
     *
     * @param sender 指令发送者
     * @param args   参数列表
     * @return 处理后的参数
     */
    @Override
    public Object parseArgument(CommandSender sender, String[] args) {
        for (String s : this.config.enmuList) {
            if (this.config.caseSensitive ? s.equals(args[0]) : s.equalsIgnoreCase(args[0])) {
                return s;
            }
        }
        return null;
    }

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
        return this.config.enmuList.toArray(new String[0]);
    }

    /**
     * 获取参数数量
     * 输入参数数组,返回将会处理的参数数量
     *
     * @param unprocessedArgs 未处理的参数数组
     * @return 占用参数数量
     */
    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        return 1;
    }
}
