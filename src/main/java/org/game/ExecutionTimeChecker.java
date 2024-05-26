package org.game;

public class ExecutionTimeChecker {
    private long executionStart;

    public void start() {
        executionStart = System.currentTimeMillis();
    }

    public long stop(String methodName) {
        long duration = System.currentTimeMillis() - executionStart;
        //System.out.println(methodName + ": " + duration);
        return duration;
    }
}
