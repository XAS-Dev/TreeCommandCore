package xyz.xasmc.testtreecommand;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xasmc.treecommand.TreeCommand;
import xyz.xasmc.treecommand.node.argument.ArgumentType;

public class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        TreeCommand exampleCommand = new TreeCommand((state, next) -> {
            state.getSender().sendMessage("success");
            boolean result = next.apply();
            return result;
        });

        // @formatter:off
         exampleCommand
                 .terminable()
                 .subCommand("help",(state, next) -> {
                     boolean result = next.apply();
                     state.getSender().sendMessage("help");
                     return result;
                 })
                 .end()
                 .subCommand("player", (state,next)->{
                     boolean result = next.apply();
                     Player player = (Player) state.getState("player");
                     state.getSender().sendMessage(String.format("player: %s, uuid: %s", player.getName(), player.getUniqueId()));
                     return result;
                 })
                         .argument(ArgumentType.PLAYER, "player")
                         .end()
                 .end()
                 .subCommand("pos",((state, next) -> {
                     boolean result = next.apply();
                     state.getSender().sendMessage("success");
                     return result;
                 }))
                         .argument(ArgumentType.POSITION, "position")
                 .end()
         ;
         // @formatter:on

        this.getCommand("examplecommand").setExecutor(exampleCommand);
        this.getCommand("examplecommand").setTabCompleter(exampleCommand);
    }
}
