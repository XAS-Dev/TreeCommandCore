package xyz.xasmc.treecommand.core.middleware;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;
import xyz.xasmc.treecommand.core.node.marker.Executable;
import xyz.xasmc.treecommand.core.node.marker.Parsable;

import java.util.*;

public class State {
    RootNode rootNode; // 根节点
    CommandSender sender = null; // 指令发送者
    String label = null; // 指令标签
    String[] args = null; // 参数列表
    List<BaseNode> executableNodeList = new ArrayList<>(); // 可执行节点列表(第一个元素一定是根节点)
    StateException exception = null; // 错误信息
    int processedArgsCount = 0;// 处理完的参数数量
    BaseNode lastNode = null;
    Map<String, Object> state = new HashMap<>();// 状态Map

    // ===== exception =====

    protected void setException(BaseNode lastNode, BaseNode failedNode, StateExceptionReason exceptionReason,
            int failedStartIndex) {
        this.lastNode = lastNode;
        this.exception = new StateException(failedNode, exceptionReason, failedStartIndex);
    }

    protected void setException(BaseNode lastNode, StateExceptionReason exceptionReason, int failedStartIndex) {
        this.lastNode = lastNode;
        this.exception = new StateException(exceptionReason, failedStartIndex);
    }

    protected boolean isSuccess() {
        return this.exception == null;
    }

    // ===== load =====

    /**
     * 加载状态
     *
     * @param args 参数数组
     */
    protected boolean load(RootNode rootNode, String[] args, CommandSender sender, String label) {
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
                this.setException(node, StateExceptionReason.TOO_MANY_ARGS, this.processedArgsCount);
                return false;
            }
            if (processingArgs.length == 0) {
                // 没参数了
                if (node.isLeafNode()) {
                    // 节点没有子节点
                    this.lastNode = node; // 成功
                    return true;
                }
            }
            // 还有未处理的参数,还有子节点
            // 遍历子节点,寻找匹配的项
            boolean isParseSuccess = false;
            for (BaseNode child : node.getChildren()) {
                int argsQuantity = child instanceof Parsable ? ((Parsable) child).getArgsQuantity(processingArgs) : 0;
                if (argsQuantity != -1) {
                    // 匹配成功
                    if (argsQuantity <= processingArgs.length) {
                        // 参数足够
                        if (child instanceof Parsable) {
                            // 可处理的
                            // 参数足够,处理参数并添加到状态Map中
                            this.state.put(child.getNodeName(),
                                    ((Parsable) child).parseArgument(sender, processingArgs));
                        }
                        // 重新设置处理中的参数数组
                        this.processedArgsCount += argsQuantity;
                        processingArgs = Arrays.copyOfRange(processingArgs, argsQuantity, processingArgs.length);
                        // 清空错误信息
                        this.exception = null;
                        // 下一层节点
                        node = child;
                        isParseSuccess = true;
                        break;
                    } else {
                        // 参数不足,不完整
                        // 添加错误信息,不返回错误
                        // 继续遍历下面的节点,如果后面的节点匹配成功会覆盖错误信息
                        this.setException(node, child, StateExceptionReason.NOT_COMPLETE, this.processedArgsCount);
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
                    this.setException(node, StateExceptionReason.TOO_FEW_ARGS, this.processedArgsCount);
                } else {
                    // 还有未处理的参数
                    // 错误的参数
                    this.setException(node, StateExceptionReason.WRONG_ARGS, this.processedArgsCount);
                }
                return false;
            }
        }

    }

    // ===== state =====

    public Block getBlock(String key) {
        return (Block) this.state.get(key);
    }

    public String getEnum(String key) {
        return (String) this.state.get(key);
    }

    public Location getLocation(String key) {
        return (Location) this.state.get(key);
    }

    public OfflinePlayer getOfflinePlayer(String key) {
        return (OfflinePlayer) this.state.get(key);
    }

    public Player getPlayer(String key) {
        return (Player) this.state.get(key);
    }

    @SuppressWarnings("unchecked")
    public List<Entity> getSelector(String key) {
        return (List<Entity>) this.state.get(key);
    }
}
