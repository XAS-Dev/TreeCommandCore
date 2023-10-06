package xyz.xasmc.treecommand.core.template;

import xyz.xasmc.treecommand.core.node.config.SubCommandNodeConfig;
import xyz.xasmc.treecommand.core.node.impl.SubCommandNode;

public class Template {
    static SubCommandNode createSubCommand(SubCommandNodeConfig config) {
        SubCommandNode result = new SubCommandNode();
        result.setConfig(config);
        return result;
    }

    static SubCommandNode createSubCommand(String label) {
        return Template.createSubCommand(new SubCommandNodeConfig(label));
    }
}
