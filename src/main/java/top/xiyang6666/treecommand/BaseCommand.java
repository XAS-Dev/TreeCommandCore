package top.xiyang6666.treecommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
    TreeCommand treeCommand = null;


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return this.treeCommand.getNodeCommand(strings).runCommand(commandSender, command, s, strings);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        StringBuilder test = new StringBuilder("|");
        for (String str : strings) {
            test.append(str);
            test.append("|");
        }
        commandSender.sendMessage(test.toString() + '-' + strings[strings.length - 1] + '-');
        List<String> emptyList = new ArrayList<String>();
        // 获取命令节点
        String[] tempStrings = Arrays.copyOfRange(strings, 0, strings.length - 1); // 忽略最后一个参数,输入完整的部分
        String lastString = strings[strings.length - 1]; // 最后一个参数,也就是输入了一半的部分
        NodeCommand nodeCommand = this.treeCommand.getNodeCommand(tempStrings); // 获取指令节点
        if (nodeCommand == null) {
            // 没有对应指令节点,返回空列表
            return emptyList;
        }
        List<String> subCommands = nodeCommand.getSubCommands();
        List<String> result = new ArrayList<String>();
        for (String str : subCommands) {
            // 匹配输入一半的部分,提供补全
            if (str.startsWith(lastString)) {
                result.add(str);
            }
        }
        return result;
    }
}
