package xyz.xasmc.treecommand.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xasmc.treecommand.core.middleware.Context;
import xyz.xasmc.treecommand.core.middleware.MiddlewareService;
import xyz.xasmc.treecommand.core.middleware.StateException;
import xyz.xasmc.treecommand.core.middleware.StateExceptionReason;
import xyz.xasmc.treecommand.core.middleware.functional.Middleware;
import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.RootNode;
import xyz.xasmc.treecommand.core.node.marker.Parseable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeCommand extends RootNode implements TabExecutor {
    protected boolean showCompletionAfterFinish = false;

    public TreeCommand() {
    }

    public TreeCommand(Middleware middleware) {
        this.setMiddleware(middleware);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Context context = MiddlewareService.createContext(this, args, sender, label);

        if (context.isSuccess()) {
            MiddlewareService.loadContext(context).next();
        } else {
            this.onError(context);
        }
        return true;
    }

    /**
     * 处理错误
     */
    public void onError(Context context) {
        CommandSender sender = context.getSender();
        String label = context.getLabel();
        String[] args = context.getArgs();
        switch (context.getException().getReason()) {
            case TOO_MANY_ARGS:
                sender.sendMessage(ChatColor.RED + "错误的命令参数");
                sender.sendMessage(this.getErrorPrompt(label, args, context.getException().getFailedStartIndex()));
                break;
            case TOO_FEW_ARGS:
                sender.sendMessage(ChatColor.RED + "未知或不完整的指令");
                sender.sendMessage(this.getErrorPrompt(label, args, context.getException().getFailedStartIndex()));
                break;
            case WRONG_ARGS:
                context.getLastNode().getChildren().get(0);
                sender.sendMessage(ChatColor.RED + "需要...");// 未知的..类型
                sender.sendMessage(this.getErrorPrompt(label, args, context.getException().getFailedStartIndex()));
                break;
            case NOT_COMPLETE:
                context.getLastNode().getChildren().get(0);
                sender.sendMessage(ChatColor.RED + "不完整（需要...个...）");// 未知的..类型
                sender.sendMessage(this.getErrorPrompt(label, args, context.getException().getFailedStartIndex()));
                break;
        }
    }

    /**
     * 获取错误位置提示
     *
     * @param label           指令名
     * @param args            参数
     * @param errorStartIndex 错误开始的索引
     * @return 错误提示
     */
    public String getErrorPrompt(String label, String[] args, int errorStartIndex) {
        List<String> commandAndArgsList = new ArrayList<>();
        commandAndArgsList.add(label);
        commandAndArgsList.addAll(Arrays.asList(args));
        StringBuilder resultBuilder = new StringBuilder();
        for (String s : commandAndArgsList.subList(0, errorStartIndex + 1)) {
            resultBuilder.append(s).append(" ");
        }
        String result = resultBuilder.toString();
        if (result.length() > 10) {
            result = "..." + result.substring(result.length() - 10);// 只取后10个
        }
        result += ChatColor.RED;
        result += ChatColor.UNDERLINE;
        result += String.join(" ", commandAndArgsList.subList(errorStartIndex + 1, commandAndArgsList.size()));
        result += ChatColor.ITALIC;
        result += "<--[此处]";
        return result;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Context context = MiddlewareService.createContext(this, Arrays.copyOfRange(args, 0, args.length - 1), sender, label);
        String lastArg = args[args.length - 1];
        StateException stateException = context.getException();
        StateExceptionReason stateExceptionReason = stateException == null ? null : stateException.getReason();
        if (stateExceptionReason == StateExceptionReason.WRONG_ARGS) return new ArrayList<>();// 错误的参数 返回空数组
        List<String> completeList = new ArrayList<>();
        for (BaseNode child : context.getLastNode().getChildren()) {
            // 获取所有Parseable Node 的补全并拼接
            if (!(child instanceof Parseable)) continue;
            Parseable parseableChild = (Parseable) child;
            String[] needfulArgs = Arrays.copyOfRange(args, context.getProcessedArgsCount(), args.length);// 获取输入的参数
            String[] childComplete = parseableChild.getCompletion(sender, needfulArgs);// 获取补全
            if (childComplete == null) continue;// 没有补全,跳过,处理下一个节点
            completeList.addAll(Arrays.asList(childComplete));// 添加补全
        }
        // 返回头部匹配的补全
        List<String> result = new ArrayList<>();
        for (String complete : completeList) {
            // 开头匹配并且不完全匹配或完全匹配且允许完全匹配时显示补全
            if ((complete.startsWith(lastArg) && !(complete.equals(lastArg))) || (complete.equals(lastArg) && this.showCompletionAfterFinish)) {
                result.add(complete);
            }
        }
        return result;
    }
}
