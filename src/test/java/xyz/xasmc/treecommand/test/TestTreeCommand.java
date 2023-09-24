package xyz.xasmc.treecommand.test;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.core.TreeCommand;
import xyz.xasmc.treecommand.core.node.type.NodeType;

public class TestTreeCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("TestTreeCommand 已加载");

        TreeCommand testTreeCommand = new TreeCommand(((state, next) -> {
            state.getSender().sendMessage("success");
            boolean applied = next.apply();
            return applied;
        }));

        // @formatter:off
        testTreeCommand
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
                    Player player = (Player) state.getState("player");
                    player.chat("qwq");
                    return applied;
                })
                        .addArgument(NodeType.PLAYER,"player").end() // 为子指令添加参数;使用end()返回父节点
                .end() // 使用end()返回父节点
                .addSubCommand("pos",((state, next) -> {
                    boolean applied = next.apply();
                    state.getSender().sendMessage("success");
                    return applied;
                }))
                        .addArgumentAndEnd(NodeType.POSITION,"position") // 简化写法,添加参数,返回源节点
        ;
        // @formatter:on

        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }

    @Override
    public void onDisable() {

    }
}
