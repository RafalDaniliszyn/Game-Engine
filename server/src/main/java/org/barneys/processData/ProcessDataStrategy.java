package org.barneys.processData;

import org.barneys.model.BaseModel;
import org.barneys.processData.gameStateProcess.GameStateStrategy;

import java.util.List;

public class ProcessDataStrategy {
    private static final List<ProcessData> processDataStrategyList;

    static {
        processDataStrategyList = List.of(
                new GameStateStrategy(),
                new InitModelStrategy()
        );
    }

    public static ProcessData getStrategy(BaseModel model) {
        for (ProcessData strategy : processDataStrategyList) {
            if (strategy.getDataType().equals(model.getDataType())) {
                System.out.println("strategy: " + strategy.getDataType());
                return strategy;
            }
        }
        return new DefaultProcessDataStrategy();
    }
}
