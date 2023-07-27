package top.xiyang6666.treecommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NodeCommand {
    private NodeCommand parent = null; // 父节点 为null则是根节点
    private Set<NodeCommand> children = new HashSet<>(); // 子节点集合
    private String name = null; // 节点名,也就是命令参数
    private NodeCommandExecutor executor =
        (CommandSender commandSender, Command command, String s, String[] strings) -> false;// 执行命令方法

    /**
     * 设置
     *
     * @param name 名称
     * @param parent 父节点
     * @param executor 执行函数
     */
    public void setup(String name, NodeCommand parent, NodeCommandExecutor executor) {
        this.name = name;
        this.parent = parent;
        this.executor = executor;
    }

    /**
     * 创建子命令
     *
     * @param name 名称
     * @param executor 执行函数
     * @param template 模版
     * @return this
     */
    public NodeCommand addSubCommand(String name, NodeCommandExecutor executor, NodeCommand template) {
        NodeCommand child = template;
        if (child == null) {
            child = new NodeCommand();
        }
        child.setup(name, this, executor);
        this.children.add(child);
        return this;
    }

    /**
     * 创建并设置子指令,返回子指令节点
     * 
     * @param name 名称
     * @param executor 执行函数
     * @return 子节点
     */
    public NodeCommand setSubCommand(String name, NodeCommandExecutor executor) {
        NodeCommand child = new NodeCommand();
        child.setup(name, this, executor);
        return child;
    }

    /**
     * 停止设置子指令,返回父指令节点
     * 
     * @return 父指令节点
     */
    public NodeCommand stopSet() {
        return this.parent;
    }

    /**
     * 获取所有子节点名称
     *
     * @return 子节点名称列表
     */
    public List<String> getSubCommands() {
        List<String> result = new ArrayList<String>();
        for (NodeCommand child : this.children) {
            result.add(child.getName());
        }
        result.sort(null);
        return result;
    }

    /**
     * 通过名称获取子命令节点
     *
     * @param name 子节点名称
     * @return 子节点
     */
    public NodeCommand getChildByName(String name) {
        for (NodeCommand child : this.children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * 执行指令
     *
     * @param commandSender 指令发送者
     * @param command 执行的指令
     * @param s 参数
     * @param strings 参数列表
     * @return 是否执行成功
     */
    public Boolean runCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return this.executor.apply(commandSender, command, s, strings);
    }

    /**
     * 获取节点名称
     *
     * @return 节点名称
     */
    public String getName() {
        return this.name;
    }

}
