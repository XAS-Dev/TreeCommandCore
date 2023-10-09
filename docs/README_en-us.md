# TreeCommandCore

Core part of TreeCommand.

TreeCommand is a Bukkit dependency plugin that provides an interface to simplify the command-writing portion of Bukkit plugins. The project is named "TreeCommand," but it uses a tree data structure to handle commands.

## Multi-language

- [简体中文](/README.md)
- [English](/docs/README_en-us.md)

## Getting Started

To use TreeCommand in your Bukkit plugin project, you should add "TreeCommandCore" as a dependency in your `pom.xml`. First, add XAS-Dev's Maven repository:

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

After that, you should add the TreeCommandLib, the Bukkit plugin of this project, to the plugin's dependencies. Edit `plugin.yml`:

```yaml
softdepend:
  - "TreeCommandLib"
```

If your project depends on TreeCommand, make sure [TreeCommandLib](https://github.com/XAS-Dev/TreeCommandLib) plugin is installed on the server before using your plugin.

## Usage Example

Here is an example of how to use TreeCommand in a Bukkit plugin:

```java
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
        this.getLogger().info("TestTreeCommand has been loaded");

        TreeCommand testTreeCommand = new TreeCommand((ctx, next) -> {
            ctx.addMessageToSender(String.format("%s%s===== testTreeCommand =====", ChatColor.AQUA, ChatColor.BOLD));
            next.next();
        });

        // Method 1, create a template first, then append
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
                entityTypeList.add(entity.getName());
            }
            ctx.addMessageToSender(String.join("; ", entityTypeList));
            next.next();
        });
        selectorSubCommand.addArgumentAndEnd(ArgumentType.SELECTOR, "selector");

        // Method 2, directly build the entire command
        // @formatter:off
        testTreeCommand
                .addExecuteNode((ctx, next) -> { // Add an execution node, execute the corresponding method when reached
                    ctx.addMessageToSender(ChatColor.GREEN + "✔ You successfully executed this command");
                    if (ctx.isEndHere()){ // Check if the command ends here, meaning no sub-commands were inputted
                        ctx.addMessageToSender("You did not input any sub-commands");
                    }
                    next.next(); // Process the next level of nodes
                })
                .addTerminalNode() // Add a terminal node, represents the command can end here
                .addSubCommand("help", (ctx, next) -> { // Add a sub-command, return the newly created sub-command node
                    ctx.addMessageToSender("help message");
                    next.next();
                }).end() // Use end() to end the configuration of sub-nodes
                .addSubCommandAndEnd("awa", (ctx, next) -> { // Simplified syntax, add a sub-command node and directly end the configuration of sub-command node
                    ctx.addMessageToSender("qwq");
                    next.next();
                })
                .addSubCommand("player", (ctx,next)->{
                    ctx.addMessageToSender("you selected a player");
                    Player player = ctx.get("player", Player.class); // Get the parameter processing result based on the node name
                    player.chat("selected me!");
                    next.next();
                })
                        .addArgument(ArgumentType.PLAYER, "player").end() // Add a parameter node for the sub-command node; use end() to end the configuration of the parameter node
                .end() // Use end() to end the configuration of sub-nodes
                .addSubCommand("offline_player", (ctx, next) -> {
                    ctx.addMessageToSender("you selected an offline player");
                    OfflinePlayer player = ctx.getOfflinePlayer("offline_player"); // Simplified syntax
                    ctx.addMessageToSender("his or her name is " + player.getName() + " and UUID is " + player.getUniqueId());
                    next.next();
                })
                        .addArgumentAndEnd(ArgumentType.OFFLINE_PLAYER, "offline_player") // Simplified syntax, add a parameter node for the sub-command node and directly end the configuration of the parameter node
                .end()
                .useTemplate(locationSubCommand) // Use the template created earlier
                .useTemplate(blockSubCommand)
                .useTemplate(blockSubCommand)
                .useTemplate(enumSubCommand)
                .useTemplate(selectorSubCommand)
        ;
        // @formatter:on


        // Set it as the executor and tab completer for the command
        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }
}

```

A specific example can be found in [test code](./src/test/java/xyz/xasmc/treecommand/test/TestTreeCommand.java).

Feel free to customize and extend this example to meet your specific plugin requirements.
