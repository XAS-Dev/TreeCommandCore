package xyz.xasmc.treecommand.core.node.config;

public class OfflinePlayerNodeConfig extends BaseNodeConfig {
    public boolean isCheckPlayerName = false;
    public boolean isCheckPlayedBefore = true;

    public OfflinePlayerNodeConfig() {
    }

    public OfflinePlayerNodeConfig(boolean isCheckPlayedBefore) {
        this.isCheckPlayedBefore = isCheckPlayedBefore;
    }
}
