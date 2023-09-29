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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.core.TreeCommand;
import type.node.xyz.xasmc.treecommand.core.NodeType;

public class ExampleCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("TestTreeCommand 已加载");

        TreeCommand testTreeCommand = new TreeCommand((state, next) -> {
            state.getSender().sendMessage("success");
            boolean applied = next.apply();
            return applied;
        });

        // @formatter:off
        testTreeCommand
                .addExecuteNode((state, next) -> { // 添加一个执行节点,处理到该节点时执行对应方法
                    state.getSender().sendMessage("===== testTreeCommand =====");
                    return next.apply(); // 处理下一层的节点
                })
                .addTerminalNode() // 添加一个可结束节点,代表可以在此处结束指令
                .addSubCommand("help",(state, next) -> { // 添加子指令,返回新建的子指令节点
                    boolean applied = next.apply();
                    state.getSender().sendMessage("help");
                    return applied;
                }).end() // 使用end()返回父节点
                .addSubCommandAndEnd("awa",((state, next) -> {
                    boolean applied = next.apply();
                    state.getSender().sendMessage("awa");
                    return applied;
                })) // 简化写法,添加子指令,返回原节点
                .addSubCommand("player",(state,next)->{
                    boolean applied = next.apply();
                    state.getSender().sendMessage("player");
                    Player player = state.getValue("player", Player.class);
                    player.chat("qwq");
                    return applied;
                })
                        .addArgument(NodeType.PLAYER,"player").end() // 为子指令添加参数;使用end()返回父节点
                .end() // 使用end()返回父节点
                .addSubCommand("pos",((state, next) -> {
                    boolean applied = next.apply();
                    state.getSender().sendMessage("pos");
                    state.getSender().sendMessage(state.getValue("position", Location.class).toString());
                    return applied;
                }))
                        .addArgumentAndEnd(NodeType.POSITION,"position") // 简化写法,添加参数,返回源节点
                .end()
                .addSubCommand("block",((state, next) -> {
                    boolean applied = next.apply();
                    Block block = state.getValue("block", Block.class);
                    state.getSender().sendMessage("block");
                    state.getSender().sendMessage(String.format("(%d, %d, %d) %s", block.getX(), block.getY(), block.getZ(), block.getType()));
                    return applied;
                }))
                        .addArgumentAndEnd(NodeType.BLOCK,"block")
                .end()
        ;
        // @formatter:on

        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }
}
```

Feel free to customize and expand upon this example to suit your specific plugin's needs.
