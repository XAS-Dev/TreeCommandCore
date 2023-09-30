package xyz.xasmc.treecommand.core.state;

import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;
import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

import java.util.*;

public class State {
    public RootNode rootNode; // 根节点

    public CommandSender sender = null; // 指令发送者
    public String label = null; // 指令标签
    public String[] args = null; // 参数列表

    public List<BaseNode> executableNodeList = new ArrayList<>(); // 可执行节点列表(第一个元素一定是根节点)
    public BaseNode lastNode = null; // 结束节点(最后一个成功的节点)

    public StateException exception = null; // 错误原因 成功则为null
    public BaseNode failedNode = null; // 错误节点
    public int failedStartIndex = -1;// 错误开始位置
    public int processedArgc = 0;// 处理完的参数数量

    public Map<String, Object> state = new HashMap<>();// 状态Map

    public boolean isSuccess() {
        return this.exception == null;
    }

    protected void setException(BaseNode lastNode, BaseNode failedNode, StateException exceptionReason, int failedStartIndex) {
        this.lastNode = lastNode;
        this.failedNode = failedNode;
        this.exception = exceptionReason;
        this.failedStartIndex = failedStartIndex;
    }

    protected void setException(BaseNode lastNode, StateException exceptionReason, int failedStartIndex) {
        this.lastNode = lastNode;
        this.exception = exceptionReason;
        this.failedStartIndex = failedStartIndex;
    }

    // ===== load =====

    /**
     * 加载状态
     *
     * @param args 参数数组
     */
    public boolean load(RootNode rootNode, String[] args, CommandSender sender, String label) {
        this.reload();
        this.rootNode = rootNode;
        this.args = args;
        this.sender = sender;
        this.label = label;

        BaseNode node = this.rootNode;
        String[] processingArgs = args;
        while (true) {
            if (node instanceof Executable) {
                // 是可执行节点，添加到可执行节点列表
                this.executableNodeList.add(node);
            }
            if (node.isLeafNode() && processingArgs.length != 0) {
                // 没节点,还有参数,错误
                this.setException(node, StateException.TOO_MANY_ARGS, this.processedArgc);
                return false;
            }
            if (processingArgs.length == 0) {
                // 没参数了
                if (node.isLeafNode()) {
                    // 节点没有子节点
                    this.lastNode = node;// 成功
                    return true;
                }
            }
            // 还有未处理的参数,还有子节点
            // 遍历子节点,寻找匹配的项
            boolean isParseSuccess = false;
            for (BaseNode child : node.getChildren()) {
                int argsQuantity = child instanceof Parseable ? ((Parseable) child).getArgsQuantity(processingArgs) : 0;
                if (argsQuantity != -1) {
                    // 匹配成功
                    if (argsQuantity <= processingArgs.length) {
                        // 参数足够
                        if (child instanceof Parseable) {
                            // 可处理的
                            // 参数足够,处理参数并添加到状态Map中
                            this.state.put(child.getNodeName(), ((Parseable) child).parseArgument(sender, processingArgs));
                        }
                        // 重新设置处理中的参数数组
                        this.processedArgc += argsQuantity;
                        processingArgs = Arrays.copyOfRange(processingArgs, argsQuantity, processingArgs.length);
                        // 清空错误信息
                        this.setException(null, null, null, -1);
                        // 下一层节点
                        node = child;
                        isParseSuccess = true;
                        break;
                    } else {
                        // 参数不足,不完整
                        // 添加错误信息,不返回错误
                        // 继续遍历下面的节点,如果后面的节点匹配成功会覆盖错误信息
                        this.setException(node, child, StateException.NOT_COMPLETE, this.processedArgc);
                        // 下一个节点
                        continue;// 不完整,换下一个子节点
                    }
                } else {
                    continue; // 不匹配,换下一个子节点
                }
            }
            if (isParseSuccess) {
                // 开始处理下一层节点
                continue;
            } else {
                // 子节点匹配失败
                if (!this.isSuccess()) {
                    // 遍历完了,已经存在错误,返回该错误
                    return false;
                }
                if (processingArgs.length == 0) {
                    // 没有未处理的参数
                    if (node.isTerminable()) {
                        // 节点可结束
                        // 成功
                        this.lastNode = node;
                        return true;
                    }
                    // 节点不可结束
                    // 参数过少
                    this.setException(node, StateException.TOO_FEW_ARGS, this.processedArgc);
                } else {
                    // 还有未处理的参数
                    // 错误的参数
                    this.setException(node, StateException.WRONG_ARGS, this.processedArgc);
                }
                return false;
            }
        }

    }

    protected void reload() {
        this.sender = null; // 指令发送者
        this.label = null; // 指令标签
        this.args = null; // 参数列表

        this.executableNodeList = new ArrayList<>(); // 可执行节点列表(第一个元素一定是根节点)
        this.lastNode = null; // 结束节点

        this.exception = null; // 错误原因 成功则为null
        this.failedNode = null; // 错误节点
        this.failedStartIndex = -1;// 错误开始位置
        this.processedArgc = 0;// 处理完的参数数量

        this.state = new HashMap<>();// 状态Map
    }
}
