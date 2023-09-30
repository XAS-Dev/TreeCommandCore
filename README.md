# TreeCommandCore

A Bukkit plugin dependency for simplifying the definition of commands used within Bukkit plugins.

## Getting Started

To use TreeCommandCore in your Spigot plugin project, you can add it as a dependency in your `pom.xml` file. First, add the XAS repository to your repositories section:

```xml
        <repository>
            <id>xas</id>
            <url>https://maven.xasmc.xyz/repository/xas/</url>
        </repository>
```

Next, add TreeCommandCore as a dependency:

```xml
        <dependency>
            <groupId>xyz.xasmc</groupId>
            <artifactId>TreeCommandCore</artifactId>
            <version>FILL_IN_THE_VERSION_NUMBER_HERE</version>
            <scope>compile</scope>
        </dependency>
```

Be sure to replace `FILL_IN_THE_VERSION_NUMBER_HERE` with the actual version number you want to use.

## Usage Example

Here's an example of how to use TreeCommandCore in your Java plugin:

```java
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.core.TreeCommand;
import xyz.xasmc.treecommand.core.node.ArgumentType;
import xyz.xasmc.treecommand.core.node.NodeType;
import xyz.xasmc.treecommand.core.node.config.EnumNodeConfig;

import java.util.ArrayList;
import java.util.List;

public class ExampleCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        TreeCommand exampleCommand = new TreeCommand((ctx, next) -> {
            ctx.getSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "===== exampleCommand =====");
            next.next();
        });

        // @formatter:off
        exampleCommand
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
                .addSubCommand("pos", (ctx, next) -> {
                    ctx.getSender().sendMessage("you selected a position: " + ctx.getValue("position", Location.class).toString());
                    next.next();
                })
                .addArgumentAndEnd(ArgumentType.LOCATION, "position")
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

        this.getCommand("examplecommand").setExecutor(exampleCommand);
        this.getCommand("examplecommand").setTabCompleter(exampleCommand);
    }
}
```

Feel free to customize and expand upon this example to suit your specific plugin's needs.
