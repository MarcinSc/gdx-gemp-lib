package com.gempukku.libgdx.common;

public class ValueFunction<Value, Result> implements Function<Value, Result> {
    private final Result result;

    public ValueFunction(Result result) {
        this.result = result;
    }

    @Override
    public Result evaluate(Value value) {
        return result;
    }
}
