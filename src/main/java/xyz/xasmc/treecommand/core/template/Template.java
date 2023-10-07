package xyz.xasmc.treecommand.core.template;

import xyz.xasmc.treecommand.core.middleware.functional.Middleware;
import xyz.xasmc.treecommand.core.node.config.SubCommandNodeConfig;
import xyz.xasmc.treecommand.core.node.impl.SubCommandNode;

public class Template {
    public static SubCommandNode createSubCommand(SubCommandNodeConfig config, Middleware middleware) {
        SubCommandNode result = new SubCommandNode();
        result.setConfig(config);
        result.setMiddleware(middleware);
        return result;
    }

    public static SubCommandNode createSubCommand(String label, Middleware middleware) {
        return Template.createSubCommand(new SubCommandNodeConfig(label), middleware);
    }
}
