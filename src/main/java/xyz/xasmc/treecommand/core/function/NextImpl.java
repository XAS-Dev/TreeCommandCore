package xyz.xasmc.treecommand.core.function;

import org.jetbrains.annotations.NotNull;
import xyz.xasmc.treecommand.core.state.State;

import javax.annotation.Nullable;

public class NextImpl implements Next {

    Executor executor;
    State state;
    Next next = () -> true;

    public NextImpl(@Nullable Executor executor, @NotNull State state) {
        this.executor = executor;
        this.state = state;
    }

    public void setNext(NextImpl next) {
        this.next = next;
    }

    @Override
    public boolean apply() {
        return this.executor.apply(this.state, this.next);
    }
}
