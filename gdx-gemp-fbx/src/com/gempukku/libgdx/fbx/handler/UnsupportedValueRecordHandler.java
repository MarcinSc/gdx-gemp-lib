package com.gempukku.libgdx.fbx.handler;

import com.gempukku.libgdx.fbx.stream.*;

import java.io.OutputStream;

public abstract class UnsupportedValueRecordHandler implements FbxRecordHandler{
    @Override
    public FbxRecordHandler newRecord(String name) {
        throw new UnsupportedOperationException("This record is not expected to have a sub-record");
    }

    @Override
    public void propertyValueShort(short valueShort) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void propertyValueBoolean(boolean valueBoolean) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void propertyValueInt(int valueInt) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void propertyValueFloat(float valueFloat) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void propertyValueDouble(double valueDouble) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void propertyValueLong(long valueLong) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void propertyValueString(String valueString) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public OutputStream propertyValueRawBytes(int length) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public FloatStream propertyValueFloatArray(int arrayLength) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public DoubleStream propertyValueDoubleArray(int arrayLength) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public LongStream propertyValueLongArray(int arrayLength) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public IntStream propertyValueIntArray(int arrayLength) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public BooleanStream propertyValueBooleanArray(int arrayLength) {
        throw new UnsupportedOperationException("This record is not expected to receive value of this type");
    }

    @Override
    public void endOfRecord() {

    }
}
