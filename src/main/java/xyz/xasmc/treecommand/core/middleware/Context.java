package xyz.xasmc.treecommand.core.middleware;

import xyz.xasmc.treecommand.core.node.BaseNode;

public class Context extends BaseContext {
    BaseNode nowNode = null;

    public Context(State state) {
        super(state);
    }

    // ===== api =====
    public BaseNode getNowNode() {
        return this.nowNode;
    }

    public boolean isEndHere() {
        BaseNode lastExecutableNode = this.state.executableNodeList.get(this.state.executableNodeList.size() - 1);
        return lastExecutableNode == this.getNowNode();
    }
}
