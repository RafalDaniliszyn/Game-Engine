package org.barneys.processData.inputPipeline;

public class Pipeline<IN, OUT> {
    private Pipe<IN, OUT> pipe;

    public Pipeline(Pipe<IN, OUT> pipe) {
        this.pipe = pipe;
    }

    public OUT process(IN input) {
        return pipe.process(input);
    }
}
