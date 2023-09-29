package xyz.xasmc.treecommand.core.node;

import xyz.xasmc.treecommand.core.function.Executor;
import xyz.xasmc.treecommand.core.node.impl.ExecuteNode;
import xyz.xasmc.treecommand.core.node.impl.SubCommandNode;
import xyz.xasmc.treecommand.core.node.impl.TerminalNode;

import java.util.List;

public interface NodeInter {
    // ===== setup =====

    /**
     * 添加子节点.
     *
     * @param child 子节点
     */
    void addChild(BaseNode child);

    // ===== SubCommand =====

    /**
     * 添加子指令.
     * 为该节点添加一个SubCommandNode子节点,返回新建的SubCommandNode.
     *
     * @param label 子指令名
     * @return 新建的SubCommandNode子节点
     */
    SubCommandNode addSubCommand(String label);

    /**
     * 添加子指令,返回此节点.
     * this.subCommand(...).end()的简写.
     * 为该节点添加一个SubCommandNode子节点,返回此节点.
     *
     * @param label 子指令名
     * @return this
     */
    BaseNode addSubCommandAndEnd(String label);

    /**
     * 添加子指令.
     * 为该节点添加一个SubCommandNode子节点,返回新建的SubCommandNode.
     *
     * @param label    子指令名
     * @param executor 执行器
     * @return 新建的SubCommandNode子节点
     */
    SubCommandNode addSubCommand(String label, Executor executor);

    /**
     * 添加子指令,返回此节点.
     * this.subCommand(...).end()的简写.
     * 为该节点添加一个SubCommandNode子节点,返回此节点.
     *
     * @param label    子指令名
     * @param executor 执行器
     * @return this
     */
    BaseNode addSubCommandAndEnd(String label, Executor executor);

    // ===== Argument =====

    /**
     * 添加参数.
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @return 新建的参数子节点
     */
    <T extends BaseNode> T addArgument(T template, String name);

    /**
     * 添加参数,返回此节点.
     * this.impl(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @return this
     */
    <T extends BaseNode> BaseNode addArgumentAndEnd(T template, String name);

    /**
     * 添加参数.
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param nodeType 子节点类型
     * @param name     子节点名
     * @return 新建的参数子节点
     */
    BaseNode addArgument(NodeType nodeType, String name);

    /**
     * 添加参数,返回此节点.
     * this.impl(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param nodeType 子节点类型
     * @param name     子节点名
     * @return this
     */
    BaseNode addArgumentAndEnd(NodeType nodeType, String name);

    // ===== EndNode =====

    /**
     * 添加终止节点
     *
     * @return 终止节点
     */
    TerminalNode addTerminalNode();

    // ===== ExecutableNode =====

    /**
     * 添加可执行节点
     *
     * @param template 模版
     * @return 可执行节点
     */
    ExecuteNode addExecuteNode(ExecuteNode template);

    /**
     * 添加可执行节点
     *
     * @param executor 执行器
     * @return 可执行节点
     */
    ExecuteNode addExecuteNode(Executor executor);

    // ===== CommandTree =====

    /**
     * 停止设置此节点,返回父节点
     *
     * @return 父节点
     */
    BaseNode end();

    /**
     * 是否为可结束节点(是叶节点或实现Terminable接口)
     *
     * @return 结果
     */
    boolean isTerminable();

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

    /**
     * 是否根节点(无父节点)
     *
     * @return 结果
     */
    boolean isRootNode();

    /**
     * 是否子节点(不是叶节点或根节点)
     *
     * @return 结果
     */
    boolean isChildNode();

    /**
     * 是否为叶节点(无子节点)
     *
     * @return 结果
     */
    boolean isLeafNode();


    // ===== getter,setter =====

    String getNodeName();

    // BaseNode setNodeName(String name);
    //
    // BaseNode setParent(BaseNode parent);
}
