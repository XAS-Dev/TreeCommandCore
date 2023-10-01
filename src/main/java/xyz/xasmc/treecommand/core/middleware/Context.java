package xyz.xasmc.treecommand.core.middleware;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.xasmc.treecommand.core.node.BaseNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Context extends BaseContext {
    protected BaseNode nowNode = null;
    protected final List<BaseComponent> messageToSender = new ArrayList<>();
    protected final List<BaseComponent> messageToBroadcast = new ArrayList<>();

    public Context(State state) {
        super(state);
    }

    // ===== api =====
    public BaseNode getNowNode() {
        return this.nowNode;
    }

    public boolean isEndHere() {
        BaseNode lastExecutableNode = this.state.executableNodeList.get(this.state.executableNodeList.size() - 1);
        return lastExecutableNode == this.getNowNode();
    }

    // ===== message to sender =====

    public List<BaseComponent> getMessageToSender() {
        return this.messageToSender;
    }

    public void addMessageToSender(BaseComponent... messages) {
        if (!this.messageToSender.isEmpty()) this.messageToSender.add(new TextComponent("\n"));
        this.messageToSender.addAll(Arrays.asList(messages));
    }

    public void addMessageToSender(boolean warp, BaseComponent... messages) {
        if (!this.messageToSender.isEmpty() & warp) this.messageToSender.add(new TextComponent("\n"));
        this.messageToSender.addAll(Arrays.asList(messages));
    }

    public void addMessageToSender(String message) {
        if (!this.messageToSender.isEmpty()) this.messageToSender.add(new TextComponent("\n"));
        this.messageToSender.addAll(Arrays.asList(TextComponent.fromLegacyText(message)));
    }

    public void addMessageToSender(boolean warp, String message) {
        if (!this.messageToSender.isEmpty() & warp) this.messageToSender.add(new TextComponent("\n"));
        this.messageToSender.addAll(Arrays.asList(TextComponent.fromLegacyText(message)));
    }

    // ===== message to broadcast =====

    public List<BaseComponent> getMessageToBroadcast() {
        return this.messageToBroadcast;
    }

    public void addMessageToBroadcast(BaseComponent... messages) {
        if (!this.messageToBroadcast.isEmpty()) this.messageToBroadcast.add(new TextComponent("\n"));
        this.messageToBroadcast.addAll(Arrays.asList(messages));
    }

    public void addMessageToBroadcast(boolean warp, BaseComponent... messages) {
        if (!this.messageToBroadcast.isEmpty() & warp) this.messageToBroadcast.add(new TextComponent("\n"));
        this.messageToBroadcast.addAll(Arrays.asList(messages));
    }

    public void addMessageToBroadcast(String message) {
        if (!this.messageToBroadcast.isEmpty()) this.messageToBroadcast.add(new TextComponent("\n"));
        this.messageToBroadcast.addAll(Arrays.asList(TextComponent.fromLegacyText(message)));
    }

    public void addMessageToBroadcast(boolean warp, String message) {
        if (!this.messageToBroadcast.isEmpty() & warp) this.messageToBroadcast.add(new TextComponent("\n"));
        this.messageToBroadcast.addAll(Arrays.asList(TextComponent.fromLegacyText(message)));
    }
}
