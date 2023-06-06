package com.gempukku.libgdx.fbx.handler;

import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.fbx.stream.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class GatherValuesRecordHandler extends UnsupportedValueRecordHandler {
    protected Array<Object> values = new Array<>();

    @Override
    public void propertyValueShort(short valueShort) {
        values.add(valueShort);
    }

    @Override
    public void propertyValueBoolean(boolean valueBoolean) {
        values.add(valueBoolean);
    }

    @Override
    public void propertyValueInt(int valueInt) {
        values.add(valueInt);
    }

    @Override
    public void propertyValueFloat(float valueFloat) {
        values.add(valueFloat);
    }

    @Override
    public void propertyValueDouble(double valueDouble) {
        values.add(valueDouble);
    }

    @Override
    public void propertyValueLong(long valueLong) {
        values.add(valueLong);
    }

    @Override
    public void propertyValueString(String valueString) {
        values.add(valueString);
    }

    @Override
    public OutputStream propertyValueRawBytes(int length) {
        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                super.close();
                values.add(toByteArray());
            }
        };
    }

    @Override
    public FloatStream propertyValueFloatArray(int arrayLength) {
        return new FloatStream() {
            private final FloatArray floatArray = new FloatArray();

            @Override
            public void write(float value) {
                floatArray.add(value);
            }

            @Override
            public void close() {
                values.add(floatArray);
            }
        };
    }

    @Override
    public DoubleStream propertyValueDoubleArray(int arrayLength) {
        return new DoubleStream() {
            private final Array<Double> doubleArray = new Array<>();

            @Override
            public void write(double value) {
                doubleArray.add(value);
            }

            @Override
            public void close() {
                values.add(doubleArray);
            }
        };
    }

    @Override
    public LongStream propertyValueLongArray(int arrayLength) {
        return new LongStream() {
            private final LongArray floatArray = new LongArray();

            @Override
            public void write(long value) {
                floatArray.add(value);
            }

            @Override
            public void close() {
                values.add(floatArray);
            }
        };
    }

    @Override
    public IntStream propertyValueIntArray(int arrayLength) {
        return new IntStream() {
            private final IntArray intArray = new IntArray();

            @Override
            public void write(int value) {
                intArray.add(value);
            }

            @Override
            public void close() {
                values.add(intArray);
            }
        };
    }

    @Override
    public BooleanStream propertyValueBooleanArray(int arrayLength) {
        return new BooleanStream() {
            private final BooleanArray booleanArray = new BooleanArray();

            @Override
            public void write(boolean value) {
                booleanArray.add(value);
            }

            @Override
            public void close() {
                values.add(booleanArray);
            }
        };
    }

    public float getFloat(int index) {
        return ((Number) values.get(index)).floatValue();
    }
}
