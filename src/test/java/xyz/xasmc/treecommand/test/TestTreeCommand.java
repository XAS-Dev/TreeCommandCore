package xyz.xasmc.treecommand.test;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.core.TreeCommand;
import xyz.xasmc.treecommand.core.node.NodeType;

public class TestTreeCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("TestTreeCommand 已加载");

        TreeCommand testTreeCommand = new TreeCommand(((state, next) -> {
            state.getSender().sendMessage("success");
            boolean applied = next.apply();
            return applied;
        }));

        // @formatter:off
        testTreeCommand
                .addExecuteNode(((state, next) -> { // 添加一个执行节点,处理到该节点时执行对应方法
                    state.getSender().sendMessage("===== testTreeCommand =====");
                    return next.apply(); // 处理下一层的节点
                }))
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
                    Player player = state.getState("player", Player.class);
                    player.chat("qwq");
                    return applied;
                })
                        .addArgument(NodeType.PLAYER,"player").end() // 为子指令添加参数;使用end()返回父节点
                .end() // 使用end()返回父节点
                .addSubCommand("offline_player",(state, next) -> {
                    boolean applied = next.apply();
                    state.getSender().sendMessage("offline_player");
                    Player player = state.getState("offline_player", Player.class);
                    state.getSender().sendMessage(player.getName() + " " + player.getUniqueId());
                    return applied;
                })
                        .addArgument(NodeType.OFFLINE_PLAYER,"offline_player").end()
                .end()
                .addSubCommand("pos",((state, next) -> {
                    boolean applied = next.apply();
                    state.getSender().sendMessage("pos");
                    state.getSender().sendMessage(state.getState("position", Location.class).toString());
                    return applied;
                }))
                        .addArgumentAndEnd(NodeType.POSITION,"position") // 简化写法,添加参数,返回源节点
                .end()
                .addSubCommand("block",((state, next) -> {
                    boolean applied = next.apply();
                    Block block = state.getState("block", Block.class);
                    state.getSender().sendMessage("block");
                    state.getSender().sendMessage(String.format("(%d, %d, %d) %s", block.getX(), block.getY(), block.getZ(), block.getType()));
                    return applied;
                }))
                .addArgumentAndEnd(NodeType.BLOCK,"block") // 简化写法,添加参数,返回源节点
                .end()
        ;
        // @formatter:on

        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }

    @Override
    public void onDisable() {

    }
}
