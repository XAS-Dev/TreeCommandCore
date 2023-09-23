package xyz.xasmc.treecommand.node.argument;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.node.ExecutableNode;

import javax.annotation.Nullable;

public class SubCommandNode extends ExecutableNode {
    protected String label = null;
    protected boolean caseSensitive = true;

    /**
     * 初始化
     */
    public SubCommandNode() {
    }

    /**
     * 初始化
     */
    public SubCommandNode(String label) {
        this.label = label;
    }

    /**
     * 初始化
     */
    public SubCommandNode(String label, boolean caseSensitive) {
        this.label = label;
        this.caseSensitive = caseSensitive;
    }

    /**
     * 获取补全数组
     *
     * @param args 参数列表
     * @return 补全数组
     */

    @Nullable
    @Override
    public String[] getCompletion(CommandSender sender, String[] args) {
        return new String[]{this.label};
    }

    /**
     * 获取改节点占用的指令参数数量
     *
     * @param unprocessedArgs 到该节点时还未处理的参数数组
     * @return 占用参数数量(可以超出参数数组长, - 1 则为错误)
     */
    @Override
    public int getArgsQuantity(String[] unprocessedArgs) {
        if (this.caseSensitive) return unprocessedArgs[0].equals(this.label) ? 1 : -1;
        else return unprocessedArgs[0].equalsIgnoreCase(this.label) ? 1 : -1;
    }


    // ===== custom =====

    /**
     * 设置是否区分大小写
     *
     * @param caseSensitive 是否区分大小写
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
