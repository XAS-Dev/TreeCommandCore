package xyz.xasmc.treecommand.core.node;

import xyz.xasmc.treecommand.core.function.Executor;
import xyz.xasmc.treecommand.core.node.impl.BaseExecutableImpl;
import xyz.xasmc.treecommand.core.node.inter.Executable;
import xyz.xasmc.treecommand.core.node.inter.Terminable;
import xyz.xasmc.treecommand.core.node.type.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode extends BaseExecutableImpl implements NodeInter {
    protected String nodeName = null;
    protected BaseNode parent = null;
    protected List<BaseNode> children = new ArrayList<>();

    // ===== setup =====
    @Override
    public void addChild(BaseNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    // ===== SubCommand =====
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
    public SubCommandNode addSubCommand(String label, Executor executor) {
        SubCommandNode child = this.addSubCommand(label);
        child.setExecutor(executor);
        return child;
    }

    @Override
    public BaseNode addSubCommandAndEnd(String label, Executor executor) {
        this.addSubCommand(label, executor);
        return this;
    }

    // ===== Argument =====

    @Override
    public <T extends BaseNode> T addArgument(T template, String name) {
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
        BaseNode child = null;
        switch (nodeType) {
            case ALL_PLAYER:
                child = new AllPlayerNode();
                break;
            // case Block:
            //     child = new BlockNode();
            //     break;
            // case Enum:
            //     child = new EnumNode();
            //     break;
            case PLAYER:
                child = new PlayerNode();
                break;
            case POSITION:
                child = new PositionNode();
                break;
            // case Selector:
            //     child = new SelectorNode();
            //     break;
            default:
                return null;
        }
        return this.addArgument(child, name);
    }

    @Override
    public BaseNode addArgumentAndEnd(NodeType nodeType, String name) {
        this.addArgument(nodeType, name);
        return this;
    }

    // ===== EndNode =====

    public TerminalNode addTerminalNode() {
        TerminalNode child = new TerminalNode();
        child.setNodeName("TERMINAL_NODE:" + child.hashCode());
        this.addChild(child);
        return child;
    }

    // ===== ExecutableNode =====

    public Executable addExecuteNode(Executable template, String name) {
        template.setNodeName(name);
        this.addChild((BaseNode) template);
        return template;
    }

    public Executable addExecuteNode(Executor executor, String name) {
        ExecuteNode child = new ExecuteNode();
        return this.addExecuteNode(child, name);
    }

    // ===== CommandTree =====

    @Override
    public BaseNode end() {
        return this.parent;
    }

    @Override
    public boolean isTerminable() {
        return this.isLeafNode() || this instanceof Terminable;
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
        while (true) {
            if (node instanceof RootNode) {
                return (RootNode) node;
            } else {
                node = node.getParent();
            }
        }
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

    @Override
    public BaseNode setNodeName(String name) {
        this.nodeName = name;
        return this;
    }

    @Override
    public BaseNode setParent(BaseNode parent) {
        this.parent = parent;
        return this;
    }
}
