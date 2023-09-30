package xyz.xasmc.treecommand.core.node;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;
import xyz.xasmc.treecommand.core.node.config.BaseNodeConfig;
import xyz.xasmc.treecommand.core.node.config.SubCommandNodeConfig;
import xyz.xasmc.treecommand.core.node.impl.ExecuteNode;
import xyz.xasmc.treecommand.core.node.impl.SubCommandNode;
import xyz.xasmc.treecommand.core.node.impl.TerminalNode;
import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.util.Argument;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode {
    protected String nodeName = null;
    protected BaseNode parent = null;
    protected List<BaseNode> children = new ArrayList<>();
    protected BaseNodeConfig config = null;
    protected Middleware middleware = (ctx, next) -> next.next();

    // ===== setup =====

    /**
     * 添加子节点.
     *
     * @param child 子节点
     */
    public void addChild(BaseNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * 设置节点配置
     *
     * @param config 节点配置
     */
    public <T extends BaseNodeConfig> void setConfig(T config) {
        this.config = config;
        this.initConfig();
    }

    /**
     * 初始化配置
     */
    public void initConfig() {
    }

    // ===== addSubCommand =====

    /**
     * 添加子指令.
     * 为该节点添加一个SubCommandNode子节点,返回新建的SubCommandNode.
     *
     * @param label 子指令名
     * @return 新建的SubCommandNode子节点
     */
    public SubCommandNode addSubCommand(String label) {
        SubCommandNodeConfig config = new SubCommandNodeConfig();
        config.label = label;
        // setup child
        SubCommandNode child = new SubCommandNode();
        child.setConfig(config);
        child.setNodeName("SUB_COMMAND:" + label);
        this.addChild(child);
        return child;
    }

    /**
     * 添加子指令,返回此节点.
     * this.subCommand(...).end()的简写.
     * 为该节点添加一个SubCommandNode子节点,返回此节点.
     *
     * @param label 子指令名
     * @return this
     */
    public BaseNode addSubCommandAndEnd(String label) {
        this.addSubCommand(label);
        return this;
    }

    /**
     * 添加子指令.
     * 为该节点添加一个SubCommandNode子节点,返回新建的SubCommandNode.
     *
     * @param label      子指令名
     * @param middleware 执行器
     * @return 新建的SubCommandNode子节点
     */
    public SubCommandNode addSubCommand(String label, Middleware middleware) {
        SubCommandNode child = this.addSubCommand(label);
        child.setMiddleware(middleware);
        return child;
    }

    /**
     * 添加子指令,返回此节点.
     * this.subCommand(...).end()的简写.
     * 为该节点添加一个SubCommandNode子节点,返回此节点.
     *
     * @param label      子指令名
     * @param middleware 执行器
     * @return this
     */
    public BaseNode addSubCommandAndEnd(String label, Middleware middleware) {
        this.addSubCommand(label, middleware);
        return this;
    }

    // ===== addArgument =====

    /**
     * 添加参数.
     * <p>
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @return 新建的参数子节点
     */
    public BaseNode addArgument(BaseNode template, String name) {
        template.setNodeName(name);
        this.addChild(template);
        return template;
    }

    /**
     * 添加参数,返回此节点.
     * <p>
     * this.impl(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @return this
     */
    public BaseNode addArgumentAndEnd(BaseNode template, String name) {
        this.addArgument(template, name);
        return this;
    }

    /**
     * 添加参数.
     * <p>
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param argumentType 子节点类型
     * @param name         子节点名
     * @return 新建的参数子节点
     */
    public BaseNode addArgument(ArgumentType argumentType, String name) {
        return this.addArgument(Argument.getArgumentByType(argumentType), name);
    }

    /**
     * 添加参数,返回此节点.
     * <p>
     * this.impl(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param argumentType 子节点类型
     * @param name         子节点名
     * @return this
     */
    public BaseNode addArgumentAndEnd(ArgumentType argumentType, String name) {
        this.addArgument(argumentType, name);
        return this;
    }

    /**
     * 添加参数.
     * <p>
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @param config   节点配置
     * @return 新建的参数子节点
     */
    public BaseNode addArgument(BaseNode template, String name, BaseNodeConfig config) {
        template.setNodeName(name);
        template.setConfig(config);
        this.addChild(template);
        return template;
    }

    /**
     * 添加参数,返回此节点.
     * <p>
     * this.impl(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param template 子节点模版
     * @param name     子节点名
     * @param config   节点配置
     * @return this
     */
    public BaseNode addArgumentAndEnd(BaseNode template, String name, BaseNodeConfig config) {
        this.addArgument(template, name, config);
        return this;
    }

    /**
     * 添加参数.
     * <p>
     * 为该节点添加一个子节点,返回新建的节点.
     *
     * @param argumentType 子节点类型
     * @param name         子节点名
     * @param config       节点配置
     * @return 新建的参数子节点
     */
    public BaseNode addArgument(ArgumentType argumentType, String name, BaseNodeConfig config) {
        return this.addArgument(Argument.getArgumentByType(argumentType), name, config);
    }

    /**
     * 添加参数,返回此节点.
     * <p>
     * this.impl(xxx).end()的简写.
     * 添加一个参数子节点,返回此节点.
     *
     * @param argumentType 子节点类型
     * @param name         子节点名
     * @param config       节点配置
     * @return this
     */
    public BaseNode addArgumentAndEnd(ArgumentType argumentType, String name, BaseNodeConfig config) {
        this.addArgument(argumentType, name, config);
        return this;
    }

    // ===== addTerminalNode =====

    /**
     * 添加终止节点
     *
     * @return 终止节点
     */
    public TerminalNode addTerminalNode() {
        TerminalNode child = new TerminalNode();
        child.setNodeName("TERMINAL_NODE:@" + child.hashCode());
        this.addChild(child);
        return child;
    }

    // ===== addExecutableNode =====

    /**
     * 添加可执行节点
     *
     * @param template 模版
     * @return 可执行节点
     */
    public ExecuteNode addExecuteNode(ExecuteNode template) {
        template.setNodeName("EXECUTE_NODE:@" + template.hashCode());
        this.addChild(template);
        return template;
    }

    /**
     * 添加可执行节点
     *
     * @param middleware 执行器
     * @return 可执行节点
     */
    public ExecuteNode addExecuteNode(Middleware middleware) {
        ExecuteNode child = new ExecuteNode();
        child.setMiddleware(middleware);
        return this.addExecuteNode(child);
    }

    // ===== CommandTree =====

    /**
     * 停止设置此节点,返回父节点
     *
     * @return 父节点
     */
    public BaseNode end() {
        return this.parent;
    }

    /**
     * 是否为可结束节点(是叶节点或实现Terminable接口)
     *
     * @return 结果
     */
    public boolean isTerminable() {
        return this.isLeafNode();
    }

    // ===== Tree =====

    public BaseNode getParent() {
        return this.parent;
    }

    public List<BaseNode> getChildren() {
        return this.children;
    }

    public RootNode getRoot() {
        BaseNode node = this;
        while (!(node instanceof RootNode)) {
            node = node.getParent();
        }
        return (RootNode) node;
    }

    public List<BaseNode> getPath() {
        List<BaseNode> pathList = new ArrayList<>();
        BaseNode node = this;
        while (true) {
            pathList.add(0, node);
            if (node instanceof RootNode) {
                break;
            } else {
                node = node.getParent();
            }
        }
        return pathList;
    }

    public int getDepth() {
        int level = 0;
        BaseNode node = this;
        while (true) {
            if (node instanceof RootNode) {
                return level;
            } else {
                level++;
                node = node.getParent();
            }
        }
    }

    public boolean isRootNode() {
        return this.parent == null;
    }

    public boolean isChildNode() {
        return !this.isRootNode() && !this.isLeafNode();
    }

    public boolean isLeafNode() {
        return this.children.isEmpty();
    }

    // ===== getter,setter =====

    public String getNodeName() {
        return this.nodeName;
    }

    public void setNodeName(String name) {
        this.nodeName = name;
    }

    public void setParent(BaseNode parent) {
        this.parent = parent;
    }

    // ===== Executable =====

    public Middleware getMiddleware() {
        if (!(this instanceof Executable)) return null;
        return this.middleware;
    }

    public void setMiddleware(Middleware middleware) {
        if (!(this instanceof Executable)) return;
        this.middleware = middleware;
    }
}
