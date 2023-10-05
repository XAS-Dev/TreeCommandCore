package xyz.xasmc.treecommand.core.node.config;

public class SubCommandNodeConfig extends BaseNodeConfig {
    public String label = "not_specified_qwq";
    public boolean caseSensitive = true;

    public SubCommandNodeConfig() {
    }

    public SubCommandNodeConfig(String label) {
        this.label = label;
    }
}
