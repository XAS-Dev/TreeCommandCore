package xyz.xasmc.treecommand.core.node.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumNodeConfig extends BaseNodeConfig {
    public List<String> enmuList = new ArrayList<>();
    public boolean caseSensitive = true;

    public EnumNodeConfig() {
    }

    public EnumNodeConfig(String[] enums) {
        this.enmuList.addAll(Arrays.asList(enums));
    }
}
