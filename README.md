# TreeCommandCore

TreeCommand 核心部分.

TreeCommand 是一个 Bukkit 依赖插件, 用于提供接口以简化 Bukkit 插件中编写指令部分的代码. 此项目叫做“TreeCommand”但是此项目使用图数据结构处理指令.

## 多语言

- [简体中文](/README.md)
- [English](/docs/README_en-us.md)

## 开始使用

要在你的 Bukkit 插件项目中使用 TreeCommand, 应该将“TreeCommandCore”作为依赖添加到 `pom.xml` 中. 首先, 添加 XAS-Dev 的 Maven 源:

```xml
<repository>
    <id>xas</id>
    <url>https://maven.xasmc.xyz/repository/xas/</url>
</repository>
```

接下来, 添加 TreeCommandCore 作为依赖:

```xml
<dependency>
    <groupId>xyz.xasmc</groupId>
    <artifactId>TreeCommandCore</artifactId>
    <version>0.1.0</version>
    <scope>provided</scope>
</dependency>
```

之后，您应该将该项目的 Bukkit 插件——TreeCommandLib 添加到插件依赖项列表中。编辑`plugin.yml`

```yaml
softdepend:
  - "TreeCommandLib"
```

如果你的项目依赖 TreeCommand, 请确保在服务器中使用你的插件前服务器中安装了[TreeCommandLib](https://github.com/XAS-Dev/TreeCommandLib)插件

## 用法示例

以下是如何在 Bukkit 插件中使用 TreeCommand 的示例：

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
                entityTypeList.add(entity.getName());
            }
            ctx.addMessageToSender(String.join("; ", entityTypeList));
            next.next();
        });
        selectorSubCommand.addArgumentAndEnd(ArgumentType.SELECTOR, "selector");

        // 方法2,直接构建整个指令
        // @formatter:off
        testTreeCommand
                .addExecuteNode((ctx, next) -> { // 添加一个执行节点,处理到该节点时执行对应方法
                    ctx.addMessageToSender(ChatColor.GREEN + "✔ You successfully executed this command");
                    if (ctx.isEndHere()) { // 判断指令是否在这里结束, 在这里结束即为没有输入任何子指令
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
                .addSubCommand("player", (ctx, next) -> {
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


        // 设置完成将其绑定在指令上
        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }
}
```

可以在[测试代码](/src/test/java/xyz/xasmc/treecommand/test/TestTreeCommand.java)中找到具体示例.

请随意定制和扩展此示例, 以满足您的特定插件需求.
