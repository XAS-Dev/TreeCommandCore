package xyz.xasmc.testtreecommand;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.TreeCommand;
import xyz.xasmc.treecommand.node.argument.ArgumentType;

public class TestTreeCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("TestTreeCommand 已加载");

        TreeCommand testTreeCommand = new TreeCommand(((state, next) -> {
            state.getSender().sendMessage("success");
            boolean result = next.apply();
            return result;
        }));

        // @formatter:off
        testTreeCommand
                .terminable()
                .subCommand("help",(state, next) -> {
                    boolean result = next.apply();
                    state.getSender().sendMessage("help");
                    return result;
                }).end()
                .subCommand("awa").end()
                .subCommand("player",(state,next)->{
                    boolean result = next.apply();
                    Player player = (Player) state.getState("player");
                    player.chat("qwq");
                    return result;
                })
                        .argument(ArgumentType.PLAYER,"player").end()
                .end()
                .subCommand("pos",((state, next) -> {
                    boolean result = next.apply();
                    state.getSender().sendMessage("success");
                    return result;
                }))
                        .argument(ArgumentType.POSITION,"position").end()
        ;
        // @formatter:on

        this.getCommand("testtreecommand").setExecutor(testTreeCommand);
        this.getCommand("testtreecommand").setTabCompleter(testTreeCommand);
    }

    @Override
    public void onDisable() {

    }
}
