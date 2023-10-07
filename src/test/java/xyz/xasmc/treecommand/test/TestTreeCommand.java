package xyz.xasmc.treecommand.test;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.core.TreeCommand;
import xyz.xasmc.treecommand.core.node.ArgumentType;
import xyz.xasmc.treecommand.core.node.impl.SubCommandNode;
import xyz.xasmc.treecommand.core.template.Template;

import java.util.ArrayList;
import java.util.List;

public class TestTreeCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("TestTreeCommand 已加载");

        TreeCommand testTreeCommand = new TreeCommand((ctx, next) -> {
            ctx.addMessageToSender(String.format("%s%s===== testTreeCommand =====", ChatColor.AQUA, ChatColor.BOLD));
            next.next();
        });

        // 方法1,先创建模版再拼接
        // Location
        SubCommandNode locationSubCommand = Template.createSubCommand("location", (ctx, next) -> {
            ctx.addMessageToSender("you selected a location: " + ctx.getLocation("location").toString());
            next.next();
        });
        locationSubCommand.addArgumentAndEnd(ArgumentType.LOCATION, "location");

        // Block
        SubCommandNode blockSubCommand = Template.createSubCommand("block", (ctx, next) -> {
            Block block = ctx.getBlock("block");
            ctx.addMessageToSender("you selected a block: " + String.format("(%d, %d, %d) %s", block.getX(), block.getY(), block.getZ(), block.getType()));
            next.next();
        });
        blockSubCommand.addArgumentAndEnd(ArgumentType.BLOCK, "block");

        // Enum
        SubCommandNode enumSubCommand = Template.createSubCommand("enum", (ctx, next) -> {
            ctx.addMessageToSender("you chose: " + ctx.get("enum", String.class));
            next.next();
        });
        blockSubCommand.addArgumentAndEnd(ArgumentType.ENUM, "enum");

        // Selector
        SubCommandNode selectorSubCommand = Template.createSubCommand("selector", (ctx, next) -> {
            ctx.addMessageToSender("you selected some entities:");
            List<Entity> entities = ctx.getSelector("selector");
            List<String> entityTypeList = new ArrayList<>(entities.size());
            for (Entity entity : entities) {
                entityTypeList.add(entity.getType().name());
            }
            ctx.addMessageToSender(String.join("; ", entityTypeList));
            next.next();
        });
        selectorSubCommand.addArgumentAndEnd(ArgumentType.SELECTOR, "selector");

        // 方法2,直接创建整个指令
        // @formatter:off
        testTreeCommand
                .addExecuteNode((ctx, next) -> { // 添加一个执行节点,处理到该节点时执行对应方法
                    ctx.addMessageToSender(ChatColor.GREEN + "✔ You successfully executed this command");
                    if (ctx.isEndHere()){ // 判断指令是否在这里结束, 在这里结束即为没有输入任何子指令
                        ctx.addMessageToSender("You did not input any sub commands");
                    }
                    next.next(); // 处理下一层的节点
                })
                .addTerminalNode() // 添加一个可结束节点,代表可以在此处结束指令
                .addSubCommand("help", (ctx, next) -> { // 添加子指令,返回新建的子指令节点
                    ctx.addMessageToSender("help message");
                    next.next();
                }).end() // 使用end()结束对子节点的设置
                .addSubCommandAndEnd("awa", (ctx, next) -> { // 简化写法,添加子指令节点,直接结束对子指令节点设置
                    ctx.addMessageToSender("qwq");
                    next.next();
                })
                .addSubCommand("player", (ctx,next)->{
                    ctx.addMessageToSender("you selected a player");
                    Player player = ctx.get("player", Player.class); // 根据节点名获取参数处理结果
                    player.chat("selected me!");
                    next.next();
                })
                        .addArgument(ArgumentType.PLAYER, "player").end() // 为子指令节点添加参数节点;使用end()结束对参数节点的设置
                .end() // 使用end()结束对子节点的设置
                .addSubCommand("offline_player", (ctx, next) -> {
                    ctx.addMessageToSender("you selected an offline player");
                    OfflinePlayer player = ctx.getOfflinePlayer("offline_player"); // 简化写法
                    ctx.addMessageToSender("his or her name is " + player.getName() + " and UUID is " + player.getUniqueId());
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.OFFLINE_PLAYER, "offline_player") // 简化写法,为子指令节点添加参数节点，直接结束对参数节点的设置
                .end()
                .useTemplate(locationSubCommand) // 使用刚才创建的模版
                .useTemplate(blockSubCommand)
                .useTemplate(blockSubCommand)
                .useTemplate(enumSubCommand)
                .useTemplate(selectorSubCommand)
        ;
        // @formatter:on


        // 设置完将其绑定在指令上
        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }
}
