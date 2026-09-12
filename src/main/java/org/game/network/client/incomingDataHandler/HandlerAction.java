package org.game.network.client.incomingDataHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerAction {
    private static final List<FutureInvoke> futureInvoke;
    private static final List<FutureInvokeWhen> futureInvokeWhen;

    static {
        futureInvoke = new ArrayList<>();
        futureInvokeWhen = new ArrayList<>();
    }

    public static synchronized void invokeAll() {
        futureInvoke.forEach(FutureInvoke::execute);
        futureInvoke.clear();
        synchronized (futureInvokeWhen) {
            futureInvokeWhen.removeIf(FutureInvokeWhen::execute);
        }
    }

    public static synchronized void add(FutureInvoke action) {
        futureInvoke.add(action);
    }

    public static synchronized void add(FutureInvokeWhen action) {
        futureInvokeWhen.add(action);
    }
}
