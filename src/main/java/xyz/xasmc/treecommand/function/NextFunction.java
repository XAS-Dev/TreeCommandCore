package xyz.xasmc.treecommand.function;

import org.jetbrains.annotations.NotNull;
import xyz.xasmc.treecommand.state.State;

import javax.annotation.Nullable;

public class NextFunction implements Next {

    Executor nextExecutor;
    State state;
    Next next = () -> true;

    public NextFunction(@Nullable Executor executor, @NotNull State state) {
        this.nextExecutor = executor;
        this.state = state;
    }

    public void setNext(NextFunction next) {
        this.next = next;
    }

    @Override
    public boolean apply() {
        return this.nextExecutor.apply(this.state, this.next);
    }
}
