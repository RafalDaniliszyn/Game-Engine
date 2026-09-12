package org.barneys.processData.inputPipeline;

@FunctionalInterface
public interface Pipe<IN, OUT> {
    OUT process(IN input);

    default <NEXT_OUT> Pipe<IN, NEXT_OUT> andThen(Pipe<? super OUT, NEXT_OUT> next) {
        return input -> next.process(process(input));
    }
}
