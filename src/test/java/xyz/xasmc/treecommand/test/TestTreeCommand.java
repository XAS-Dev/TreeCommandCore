package xyz.xasmc.treecommand.test;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.core.TreeCommand;
import xyz.xasmc.treecommand.core.node.ArgumentType;
import xyz.xasmc.treecommand.core.node.config.EnumNodeConfig;

import java.util.ArrayList;
import java.util.List;

public class TestTreeCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("TestTreeCommand 已加载");

        TreeCommand testTreeCommand = new TreeCommand((ctx, next) -> {
            ctx.getSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "===== testTreeCommand =====");
            next.next();
        });

        // @formatter:off
        testTreeCommand
                .addExecuteNode((ctx, next) -> { // 添加一个执行节点,处理到该节点时执行对应方法
                    ctx.getSender().sendMessage(ChatColor.GREEN + "✔ You successfully executed this command");
                    next.next(); // 处理下一层的节点
                })
                .addTerminalNode() // 添加一个可结束节点,代表可以在此处结束指令
                .addSubCommand("help", (ctx, next) -> { // 添加子指令,返回新建的子指令节点
                    ctx.getSender().sendMessage("help message");
                    next.next();
                }).end() // 使用end()结束对子节点的设置
                .addSubCommandAndEnd("awa", (ctx, next) -> { // 简化写法,添加子指令节点,直接结束对子指令节点设置
                    ctx.getSender().sendMessage("qwq");
                    next.next();
                })
                .addSubCommand("player", (ctx,next)->{
                    ctx.getSender().sendMessage("you selected a player");
                    Player player = ctx.getValue("player", Player.class);
                    player.chat("selected me!");
                    next.next();
                })
                        .addArgument(ArgumentType.PLAYER, "player").end() // 为子指令节点添加参数节点;使用end()结束对参数节点的设置
                .end() // 使用end()结束对子节点的设置
                .addSubCommand("offline_player", (ctx, next) -> {
                    ctx.getSender().sendMessage("you selected an offline player");
                    OfflinePlayer player = ctx.getValue("offline_player", OfflinePlayer.class);
                    ctx.getSender().sendMessage("his or her name is " + player.getName() + " and UUID is " + player.getUniqueId());
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.OFFLINE_PLAYER, "offline_player") // 简化写法,为子指令节点添加参数节点，直接结束对参数节点的设置
                .end()
                .addSubCommand("location", (ctx, next) -> {
                    ctx.getSender().sendMessage("you selected a location: " + ctx.getValue("location", Location.class).toString());
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.LOCATION, "location")
                .end()
                .addSubCommand("block", (ctx, next) -> {
                    Block block = ctx.getValue("block", Block.class);
                    ctx.getSender().sendMessage("you selected a block: " + String.format("(%d, %d, %d) %s", block.getX(), block.getY(), block.getZ(), block.getType()));
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.BLOCK, "block")
                .end()
                .addSubCommand("enum", (ctx, next) -> {
                    ctx.getSender().sendMessage("you chose: "+ ctx.getValue("enum", String.class));
                    next.next();
                })
                    .addArgumentAndEnd(ArgumentType.ENUM,"enum",new EnumNodeConfig(new String[]{"a", "b", "c"}))
                .end()
                .addSubCommand("selector", (ctx, next) -> {
                    ctx.getSender().sendMessage("you selected some entities:");
                    List<Entity> entitys = ctx.getValue("selector", List.class);
                    List<String> entityTypeList = new ArrayList<>(entitys.size());
                    for (Entity entity:entitys){
                        entityTypeList.add(entity.getType().name());
                    }
                    ctx.getSender().sendMessage(String.join("; ", entityTypeList));
                    next.next();
                })
                .addArgumentAndEnd(ArgumentType.SELECTOR, "selector")
        ;
        // @formatter:on

        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }
}
