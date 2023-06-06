package com.gempukku.libgdx.fbx.handler;

import com.gempukku.libgdx.fbx.stream.*;

import java.io.OutputStream;

public class DebugDbxRecordHandler implements FbxRecordHandler {
    private int level;

    public DebugDbxRecordHandler() {
        this(0);
    }

    public DebugDbxRecordHandler(int level) {
        this.level = level;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        for (int i=0; i<level; i++) {
            System.out.print("  ");
        }
        System.out.println("Debug record: " + name);
        level++;
        return this;
    }

    @Override
    public void propertyValueShort(short valueShort) {

    }

    @Override
    public void propertyValueBoolean(boolean valueBoolean) {

    }

    @Override
    public void propertyValueInt(int valueInt) {

    }

    @Override
    public void propertyValueFloat(float valueFloat) {

    }

    @Override
    public void propertyValueDouble(double valueDouble) {

    }

    @Override
    public void propertyValueLong(long valueLong) {

    }

    @Override
    public void propertyValueString(String valueString) {
        for (int i=0; i<level; i++) {
            System.out.print("  ");
        }
        System.out.println("Value: "+valueString);
    }

    @Override
    public OutputStream propertyValueRawBytes(int length) {
        return null;
    }

    @Override
    public FloatStream propertyValueFloatArray(int arrayLength) {
        return null;
    }

    @Override
    public DoubleStream propertyValueDoubleArray(int arrayLength) {
        return null;
    }

    @Override
    public LongStream propertyValueLongArray(int arrayLength) {
        return null;
    }

    @Override
    public IntStream propertyValueIntArray(int arrayLength) {
        return null;
    }

    @Override
    public BooleanStream propertyValueBooleanArray(int arrayLength) {
        return null;
    }

    @Override
    public void endOfRecord() {
        level--;
    }
}
