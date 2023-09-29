package xyz.xasmc.treecommand.core.node;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;
import xyz.xasmc.treecommand.core.node.impl.*;
import xyz.xasmc.treecommand.core.node.marker.Executable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode implements NodeInter {
    protected String nodeName = null;
    protected BaseNode parent = null;
    protected List<BaseNode> children = new ArrayList<>();


    public BaseNode() {

    }

    public BaseNode(String nodeName, BaseNode parent) {
        this.setNodeName(nodeName);
        this.setParent(parent);
    }

    // ===== setup =====
    @Override
    public void addChild(BaseNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    // ===== addSubCommand =====
    @Override
    public SubCommandNode addSubCommand(String label) {
        SubCommandNode child = new SubCommandNode(label);
        child.setNodeName("SUB_COMMAND:" + label);
        this.addChild(child);
        return child;
    }

    @Override
    public BaseNode addSubCommandAndEnd(String label) {
        this.addSubCommand(label);
        return this;
    }

    @Override
    public SubCommandNode addSubCommand(String label, Middleware middleware) {
        SubCommandNode child = this.addSubCommand(label);
        child.setMiddleware(middleware);
        return child;
    }

    @Override
    public BaseNode addSubCommandAndEnd(String label, Middleware middleware) {
        this.addSubCommand(label, middleware);
        return this;
    }

    // ===== addArgument =====

    @Override
    public <T extends BaseNode> T addArgument(T template, String name) {
        template.setNodeName(name);
        this.addChild(template);
        return template;
    }

    @Override
    public <T extends BaseNode> BaseNode addArgumentAndEnd(T template, String name) {
        this.addArgument(template, name);
        return this;
    }

    @Override
    public BaseNode addArgument(NodeType nodeType, String name) {
        return this.addArgument(this.getArgumentByType(nodeType), name);
    }

    @Override
    public BaseNode addArgumentAndEnd(NodeType nodeType, String name) {
        this.addArgument(nodeType, name);
        return this;
    }

    // ===== addTerminalNode =====

    public TerminalNode addTerminalNode() {
        TerminalNode child = new TerminalNode();
        child.setNodeName("TERMINAL_NODE:@" + child.hashCode());
        this.addChild(child);
        return child;
    }

    // ===== addExecutableNode =====

    public ExecuteNode addExecuteNode(ExecuteNode template) {
        template.setNodeName("EXECUTE_NODE:@" + template.hashCode());
        this.addChild(template);
        return template;
    }

    public ExecuteNode addExecuteNode(Middleware middleware) {
        ExecuteNode child = new ExecuteNode();
        child.setMiddleware(middleware);
        return this.addExecuteNode(child);
    }

    // ===== CommandTree =====

    @Override
    public BaseNode end() {
        return this.parent;
    }

    @Override
    public boolean isTerminable() {
        return this.isLeafNode();
    }

    // ===== Tree =====

    @Override
    public BaseNode getParent() {
        return this.parent;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean isRootNode() {
        return this.parent == null;
    }

    @Override
    public boolean isChildNode() {
        return !this.isRootNode() && !this.isLeafNode();
    }

    @Override
    public boolean isLeafNode() {
        return this.children.isEmpty();
    }

    // ===== getter,setter =====

    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    // @Override
    protected BaseNode setNodeName(String name) {
        this.nodeName = name;
        return this;
    }

    // @Override
    protected BaseNode setParent(BaseNode parent) {
        this.parent = parent;
        return this;
    }

    // ===== Executable =====

    protected Middleware middleware = (ctx, next) -> next.next();


    public Middleware getMiddleware() {
        if (!(this instanceof Executable)) return null;
        return this.middleware;
    }

    public void setMiddleware(Middleware middleware) {
        if (!(this instanceof Executable)) return;
        this.middleware = middleware;
    }

    // ===== custom =====

    private BaseNode getArgumentByType(NodeType type) {
        switch (type) {
            case BLOCK:
                return new BlockNode();
            // case Enum:
            //     return new EnumNode();
            case OFFLINE_PLAYER:
                return new OfflinePlayerNode();
            case PLAYER:
                return new PlayerNode();
            case POSITION:
                return new PositionNode();
            // case Selector:
            //     child = new SelectorNode();
            default:
                return null;
        }
    }
}
