package xyz.xasmc.treecommand.core.node.type;

import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.inter.Executable;

/**
 * 用于在指令树中插入一个执行器
 * 默认为可结束节点
 * 可作为指令结束点的标志,并且为可执行节点,可以在指令结束处执行相应操作
 */
public class ExecuteNode extends BaseNode implements Executable {
}
