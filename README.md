# TreeCommand

A Bukkit plugin dependency for simplifying the definition of commands used within Bukkit plugins.

## Getting Started

To use TreeCommand in your Spigot plugin project, you can add it as a dependency in your `pom.xml` file. First, add the XAS repository to your repositories section:

```xml
        <repository>
            <id>xas</id>
            <url>https://maven.xasmc.xyz/repository/xas/</url>
        </repository>
```

Next, add TreeCommand as a dependency:

```xml
        <dependency>
            <groupId>xyz.xasmc</groupId>
            <artifactId>TreeCommand</artifactId>
            <version>FILL_IN_THE_VERSION_NUMBER_HERE</version>
            <scope>compile</scope>
        </dependency>
```

Be sure to replace `FILL_IN_THE_VERSION_NUMBER_HERE` with the actual version number you want to use.

## Usage Example

Here's an example of how to use TreeCommand in your Java plugin:

```java
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.TreeCommand;
import xyz.xasmc.treecommand.node.type.NodeType;
import xyz.xasmc.treecommand.node.type.Type;

public class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        TreeCommand exampleCommand = new TreeCommand((state, next) -> {
            state.getSender().sendMessage("success");
            boolean applied = next.apply();
            return result;
        });

        // @formatter:off
         exampleCommand
                 .terminable()
                 .addSubCommand("help",(state, next) -> {
                     boolean applied = next.apply();
                     state.getSender().sendMessage("help");
                     return result;
                 })
                 .end()
                 .addSubCommand("player", (state,next)->{
                     boolean applied = next.apply();
                     Player player = (Player) state.getState("player");
                     state.getSender().sendMessage(String.format("player: %s, uuid: %s", player.getName(), player.getUniqueId()));
                     return result;
                 })
                         .addArgument(NodeType.PLAYER, "player")
                         .end()
                 .end()
                 .addSubCommand("pos",((state, next) -> {
                     boolean applied = next.apply();
                     state.getSender().sendMessage("success");
                     return result;
                 }))
                         .addArgument(NodeType.POSITION, "position")
                 .end()
         ;
         // @formatter:on

        this.getCommand("examplecommand").setExecutor(exampleCommand);
        this.getCommand("examplecommand").setTabCompleter(exampleCommand);
    }
}
```

Feel free to customize and expand upon this example to suit your specific plugin's needs.
