package xyz.xasmc.treecommand.core.state;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;
import xyz.xasmc.treecommand.core.node.inter.Executable;
import xyz.xasmc.treecommand.core.node.inter.Parseable;

import java.util.*;

public class State {
    protected RootNode rootNode; // 根节点

    protected CommandSender commandSender = null; // 指令发送者
    protected String label = null; // 指令标签
    protected String[] args = null; // 参数列表

    protected List<Executable> executableNodes = new ArrayList<>(); // 可执行节点列表(第一个元素一定是根节点)
    protected BaseNode lastNode = null; // 结束节点(最后一个成功的节点)

    protected StateError errorReason = null; // 错误原因 成功则为null
    protected BaseNode errorNode = null; // 错误节点
    protected int errorStartIndex = -1;// 错误开始位置
    protected int processedArgc = 0;// 处理完的参数数量

    protected Map<String, Object> state = new HashMap<>();// 状态Map

    // ===== init =====

    /**
     * 初始化
     *
     * @param rootNode 根节点
     */
    public State(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    // ===== getter, setter =====

    public RootNode getRootNode() {
        return this.rootNode;
    }

    public CommandSender getSender() {
        return this.commandSender;
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getArgs() {
        return this.args;
    }

    public List<Executable> getExecutableNodes() {
        return this.executableNodes;
    }

    public BaseNode getLastNode() {
        return this.lastNode;
    }

    public StateError getErrorReason() {
        return this.errorReason;
    }

    public BaseNode getErrorNode() {
        return this.errorNode;
    }

    public int getErrorStartIndex() {
        return this.errorStartIndex;
    }

    public int getProcessedArgc() {
        return this.processedArgc;
    }

    public boolean isSuccess() {
        return this.errorReason == null;
    }

    // ===== state =====

    /**
     * 获取状态值
     *
     * @param name 键
     * @return 值
     */
    public Object getState(String name) {
        return this.state.get(name);
    }

    /**
     * 获取状态Map
     *
     * @return 状态Map
     */
    public Map<String, Object> getStateMap() {
        return this.state;
    }

    // ===== load =====

    /**
     * 加载状态
     *
     * @param args 参数数组
     */
    public boolean load(String[] args, CommandSender sender, String label) {
        this.reload();
        this.args = args;
        this.commandSender = sender;
        this.label = label;

        BaseNode node = this.rootNode;
        String[] processingArgs = args;
        while (true) {
            if (node instanceof Executable) {
                // 是可执行节点，添加到可执行节点列表
                this.executableNodes.add((Executable) node);
            }
            if (node.isLeafNode() && processingArgs.length != 0) {
                // 没节点,还有参数,错误
                this.setError(node, StateError.TOO_MANY_ARGS, processedArgc);
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
                        this.setError(null, null, null, -1);
                        // 下一层节点
                        node = child;
                        isParseSuccess = true;
                        break;
                    } else {
                        // 参数不足,不完整
                        // 添加错误信息,不返回错误
                        // 继续遍历下面的节点,如果后面的节点匹配成功会覆盖错误信息
                        this.setError(node, child, StateError.NOT_COMPLETE, processedArgc);
                        // 下一个节点
                        Bukkit.getLogger().info(child.getNodeName() + " 不完整");
                        continue;// 不完整,换下一个子节点
                    }
                } else {
                    Bukkit.getLogger().info(child.getNodeName() + " 不匹配");
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
                    this.setError(node, StateError.TOO_FEW_ARGS, processedArgc);
                } else {
                    // 还有未处理的参数
                    // 错误的参数
                    this.setError(node, StateError.WRONG_ARGS, processedArgc);
                }
                return false;
            }
        }

    }

    protected void reload() {
        this.commandSender = null; // 指令发送者
        this.label = null; // 指令标签
        this.args = null; // 参数列表

        this.executableNodes = new ArrayList<>(); // 可执行节点列表(第一个元素一定是根节点)
        this.lastNode = null; // 结束节点

        this.errorReason = null; // 错误原因 成功则为null
        this.errorNode = null; // 错误节点
        this.errorStartIndex = -1;// 错误开始位置
        this.processedArgc = 0;// 处理完的参数数量

        this.state = new HashMap<>();// 状态Map
    }

    protected void setError(BaseNode lastNode, BaseNode errorNode, StateError errorReason, int errorStartIndex) {
        this.lastNode = lastNode;
        this.errorNode = errorNode;
        this.errorReason = errorReason;
        this.errorStartIndex = errorStartIndex;
    }

    protected void setError(BaseNode lastNode, StateError errorReason, int errorStartIndex) {
        this.lastNode = lastNode;
        this.errorReason = errorReason;
        this.errorStartIndex = errorStartIndex;
    }
}
