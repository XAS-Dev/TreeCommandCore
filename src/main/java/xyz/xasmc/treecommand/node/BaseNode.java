package xyz.xasmc.treecommand.node;

import org.bukkit.Bukkit;
import xyz.xasmc.treecommand.function.Executor;
import xyz.xasmc.treecommand.node.argument.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode implements BaseNodeInterface {
    protected String nodeName = null;
    protected BaseNode parent = null;
    protected ArgumentType type = null;
    protected List<BaseNode> children = new ArrayList<>();
    protected boolean terminable = false;

    @Override
    public void init(String name, BaseNode parent) {
        this.nodeName = name;
        this.parent = parent;

        // 遍历路径 判断是否有重复的节点名
        BaseNode node = this;
        while (true) {
            node = node.getParent();
            if (node == null || node instanceof RootNode) break;
            if (node.getNodeName().equals((this.getNodeName()))) {
                Bukkit.getLogger().warning(String.format("[TreeCommand] The instruction node name %s is duplicated within the same branch, which may lead to issues in correctly reading and parsing values.", this.getNodeName()));
            }
        }

    }

    // ===== setup =====
    @Override
    public void addChild(BaseNode child) {
        this.children.add(child);
    }

    @Override
    public SubCommandNode subCommand(String label) {
        SubCommandNode child = new SubCommandNode(label);
        child.init(null, this);
        child.setNodeName("SUB_COOMAND:" + label);
        this.addChild(child);
        return child;
    }

    @Override
    public BaseNode subCommandEnd(String label) {
        this.subCommand(label);
        return this;
    }

    @Override
    public SubCommandNode subCommand(String label, Executor executor) {
        SubCommandNode child = this.subCommand(label);
        child.setExecutor(executor);
        return child;
    }

    @Override
    public BaseNode subCommandEnd(String label, Executor executor) {
        this.subCommand(label, executor);
        return this;
    }


    @Override
    public BaseNode argument(BaseNode template, String name) {
        template.init(name, this);
        this.addChild(template);
        return template;
    }

    @Override
    public BaseNode argumentEnd(BaseNode template, String name) {
        this.argument(template, name);
        return this;
    }

    @Override
    public BaseNode argument(ArgumentType type, String name) {
        BaseNode child = null;
        switch (type) {
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
        return this.argument(child, name);
    }

    @Override
    public BaseNode argumentEnd(ArgumentType type, String name) {
        this.argument(type, name);
        return this;
    }

    @Override
    public BaseNode end() {
        return this.parent;
    }

    @Override
    public BaseNode terminable(boolean flag) {
        this.terminable = flag;
        return this;
    }

    @Override
    public BaseNode terminable() {
        return this.terminable(true);
    }

    // ===== tree =====

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

    // ===== getter,setter =====

    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    @Override
    public ArgumentType getType() {
        return this.type;
    }

    @Override
    public boolean isTerminalNode() {
        return this.children.isEmpty();
    }

    @Override
    public boolean isTerminable() {
        return this.terminable || this.isTerminalNode();
    }


    /**
     * 获取所有可结束的节点
     *
     * @return 可结束的节点列表
     */
    public List<BaseNode> getAllTerminalNode() {
        List<BaseNode> result = new ArrayList<>();
        if (this.isTerminalNode()) {
            result.add(this);
            return result;
        }
        if (this.isTerminable()) {
            result.add(this);
        }
        for (BaseNode child : this.getChildren()) {
            result.addAll(child.getAllTerminalNode());
        }
        return result;
    }

    @Override
    public BaseNode setNodeName(String name) {
        this.nodeName = name;
        return this;
    }


    // ===== command =====

}
