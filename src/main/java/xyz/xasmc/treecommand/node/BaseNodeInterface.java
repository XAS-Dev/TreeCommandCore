package xyz.xasmc.treecommand.node;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.function.Executor;
import xyz.xasmc.treecommand.node.argument.ArgumentType;
import xyz.xasmc.treecommand.node.argument.SubCommandNode;

import javax.annotation.Nullable;
import java.util.List;

public interface BaseNodeInterface {

    /**
     * 初始化节点,设置节点名和父节点.
     *
     * @param name   节点名
     * @param parent 父节点
     */
    void init(String name, BaseNode parent);

    // ===== setup =====

    /**
     * 添加子节点.
     *
     * @param child 子节点
     */
    void addChild(BaseNode child);

    /**
     * 添加子指令.
     * 为该节点添加一个SubCommandNode子节点,返回新建的SubCommandNode.
     *
     * @param label 子指令名
     * @return 新建的SubCommandNode子节点
     */
    SubCommandNode subCommand(String label);

    /**
     * 添加子指令,返回此节点.
     * this.subCommand(...).end()的简写.
     * 为该节点添加一个SubCommandNode子节点,返回此节点.
     *
     * @param label 子指令名
     * @return this
     */
    BaseNode subCommandEnd(String label);

    /**
     * 添加子指令.
     * 为该节点添加一个SubCommandNode子节点,返回新建的SubCommandNode.
     *
     * @param label    子指令名
     * @param executor 执行器
     * @return 新建的SubCommandNode子节点
     */
    SubCommandNode subCommand(String label, Executor executor);

    /**
     * 添加子指令,返回此节点.
     * this.subCommand(...).end()的简写.
     * 为该节点添加一个SubCommandNode子节点,返回此节点.
     *
     * @param label    子指令名
     * @param executor 执行器
     * @return this
     */
    BaseNode subCommandEnd(String label, Executor executor);

    /**
     * 添加参数.
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @return 新建的参数子节点
     */
    BaseNode argument(BaseNode template, String name);

    /**
     * 添加参数,返回此节点.
     * this.argument(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @return this
     */
    BaseNode argumentEnd(BaseNode template, String name);

    /**
     * 添加参数.
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param type 子节点类型
     * @param name 子节点名
     * @return 新建的参数子节点
     */
    BaseNode argument(ArgumentType type, String name);

    /**
     * 添加参数,返回此节点.
     * this.argument(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param type 子节点类型
     * @param name 子节点名
     * @return this
     */
    BaseNode argumentEnd(ArgumentType type, String name);

    /**
     * 停止设置此节点,返回父节点
     *
     * @return 父节点
     */
    BaseNode end();

    /**
     * 设置此节点是否可以作为终点节点.
     * (如果是终点节点,处理参数时可以在此节点结束)
     *
     * @param flag 是否可作为终点节点
     * @return this
     */
    BaseNode terminable(boolean flag);

    /**
     * 设置此节点可以作为终点节点
     * (如果是终点节点,处理参数时可以在此节点结束)
     *
     * @return this
     */
    BaseNode terminable();


    // ===== Tree =====

    /**
     * 获取父节点
     *
     * @return 父节点
     */
    BaseNode getParent();

    /**
     * 获取子节点数组
     *
     * @return 子节点数组
     */
    List<BaseNode> getChildren();

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    RootNode getRoot();

    /**
     * 获取节点路径
     *
     * @return 路径列表
     */
    List<BaseNode> getPath();

    /**
     * 获取节点深度.
     * (根节点深度为0)
     *
     * @return 节点深度
     */
    int getDepth();


    // ===== getter,setter =====

    String getNodeName();

    ArgumentType getType();

    boolean isTerminalNode();

    boolean isTerminable();

    /**
     * 获取所有可结束的节点
     *
     * @return 可结束的节点列表
     */
    List<BaseNode> getAllTerminalNode();

    BaseNode setNodeName(String name);


    // ===== command =====

    /**
     * 获取补全数组
     *
     * @param sender 发送者
     * @param args   参数列表
     * @return 补全数组
     */
    @Nullable
    String[] getCompletion(CommandSender sender, String[] args);

    /**
     * 获取处理参数数量
     * 输入参数数组,返回要处理的参数数量
     *
     * @param unprocessedArgs 到该节点时还未处理的参数数组
     * @return 占用参数数量(可以超出参数数组长, - 1 则为错误)
     */
    int getArgsQuantity(String[] unprocessedArgs);
}
