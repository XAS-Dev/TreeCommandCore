package xyz.xasmc.treecommand.core.middleware;

import xyz.xasmc.treecommand.core.node.BaseNode;

public class StateException extends Exception {
    protected StateExceptionReason exception; // 错误原因 成功则为null
    protected BaseNode failedNode = null; // 错误节点
    protected int failedStartIndex;// 错误开始位置
    // protected BaseNode lastNode = null; // 结束节点(最后一个成功的节点)


    protected StateException(BaseNode failedNode, StateExceptionReason exceptionReason, int failedStartIndex) {
        // this.lastNode = lastNode;
        this.failedNode = failedNode;
        this.exception = exceptionReason;
        this.failedStartIndex = failedStartIndex;
    }

    protected StateException(StateExceptionReason exceptionReason, int failedStartIndex) {
        // this.lastNode = lastNode;
        this.exception = exceptionReason;
        this.failedStartIndex = failedStartIndex;
    }

    // ===== getter =====
    public StateExceptionReason getReason() {
        return this.exception;
    }

    public BaseNode getFailedNode() {
        return this.failedNode;
    }

    public int getFailedStartIndex() {
        return this.failedStartIndex;
    }

    // public BaseNode getLastNode() {
    //     return this.lastNode;
    // }
}
