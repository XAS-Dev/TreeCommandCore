package xyz.xasmc.treecommand.core.node.impl;

import xyz.xasmc.treecommand.core.node.BaseNode;
import xyz.xasmc.treecommand.core.node.marker.Terminable;

public class TerminalNode extends BaseNode implements Terminable {
    @Override
    public boolean isTerminable() {
        return true;
    }
}
