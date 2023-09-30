# TreeCommandCore

A Bukkit plugin dependency for simplifying the definition of commands used within Bukkit plugins. This project is called
TreeCommand, but it uses a graph structure to process commands

## Getting Started

To use TreeCommandCore in your Spigot plugin project, you can add it as a dependency in your `pom.xml` file. First, add
the XAS repository to your repositories section:

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
    <version>0.1.0</version>
    <scope>provided</scope>
</dependency>
```

You should then add the Bukkit plugin for this project to the dependency list. Edit the `plugin.yml`

```yaml
softdepend:
  - "TreeCommandLib"
```

If your plugin depends on this project, please make sure to install the release version of
project [TreeCommandLib](https://github.com/XAS-Dev/TreeCommandLib)'s plugin when installing your own plugin on the
server.

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
import xyz.xasmc.treecommand.core.node.config.EnumNodeConfig;

import java.util.ArrayList;
import java.util.List;

public class ExampleCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        TreeCommand exampleCommand = new TreeCommand((ctx, next) -> {
            ctx.addMessageToSender(ChatColor.AQUA + "" + ChatColor.BOLD + "===== exampleCommand =====");
            next.next();
        });

        // @formatter:off
        exampleCommand
                .addExecuteNode((ctx, next) -> { // Add an execution node, executes corresponding code when reached
                    ctx.addMessageToSender(ChatColor.GREEN + "✔ You successfully executed this command");
                    if (ctx.isEndHere()){ // Check if the command ends here, meaning no sub-commands were input
                        ctx.addMessageToSender("You did not input any sub commands");
                    }
                    next.next(); // Handle the next level of nodes
                })
                .addTerminalNode() // Add a terminal node, represents where the command can end
                .addSubCommand("help", (ctx, next) -> { // Add a sub-command, returns a newly created sub-command node
                    ctx.addMessageToSender("help message");
                    next.next();
                }).end() // Use end() to finish setting up the sub-node
                .addSubCommandAndEnd("awa", (ctx, next) -> { // Simplified form, add a sub-command node and directly end its setup
                    ctx.addMessageToSender("qwq");
                    next.next();
                })
                .addSubCommand("player", (ctx,next)->{
                    ctx.addMessageToSender("you selected a player");
                    Player player = ctx.get("player", Player.class);
                    player.chat("selected me!");
                    next.next();
                })
                        .addArgument(ArgumentType.PLAYER, "player").end() // Add a parameter node for the sub-command; use end() to finish setting up the parameter node
                        .end() // Use end() to finish setting up the sub-node
                .addSubCommand("offline_player", (ctx, next) -> {
                    ctx.addMessageToSender("you selected an offline player");
                    OfflinePlayer player = ctx.get("offline_player", OfflinePlayer.class);
                    ctx.addMessageToSender("his or her name is " + player.getName() + " and UUID is " + player.getUniqueId());
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.OFFLINE_PLAYER, "offline_player") // Simplified form, add a parameter node for the sub-command and directly end its setup
                .end()
                .addSubCommand("pos", (ctx, next) -> {
                    ctx.addMessageToSender("you selected a position: " + ctx.get("position", Location.class).toString());
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.LOCATION, "position")
                .end()
                .addSubCommand("block", (ctx, next) -> {
                    Block block = ctx.get("block", Block.class);
                    ctx.addMessageToSender("you selected a block: " + String.format("(%d, %d, %d) %s", block.getX(), block.getY(), block.getZ(), block.getType()));
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.BLOCK, "block")
                .end()
                .addSubCommand("enum", (ctx, next) -> {
                    ctx.addMessageToSender("you chose: "+ ctx.get("enum", String.class));
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.ENUM,"enum",new EnumNodeConfig(new String[]{"a", "b", "c"}))
                .end()
                .addSubCommand("selector", (ctx, next) -> {
                    ctx.addMessageToSender("you selected some entities:");
                    List<Entity> entities = ctx.get("selector", List.class);
                    List<String> entityTypeList = new ArrayList<>(entities.size());
                    for (Entity entity:entities){
                        entityTypeList.add(entity.getType().name());
                    }
                    ctx.addMessageToSender(String.join("; ", entityTypeList));
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.SELECTOR, "selector")
                .end()
        ;
        // @formatter:on

        this.getCommand("examplecommand").setExecutor(exampleCommand);
        this.getCommand("examplecommand").setTabCompleter(exampleCommand);
    }
}
```

Feel free to customize and expand upon this example to suit your specific plugin's needs.
