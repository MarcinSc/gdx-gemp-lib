package com.gempukku.libgdx.fbx;

import com.gempukku.libgdx.fbx.handler.FbxHandler;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.stream.*;

import java.io.OutputStream;

public class DebugFbxHandler implements FbxHandler {
    @Override
    public FbxRecordHandler newRecord(String name) {
        System.out.println("Top level record: " + name);
        return new DebugRecordHandler();
    }

    private static class DebugRecordHandler implements FbxRecordHandler {
        @Override
        public FbxRecordHandler newRecord(String name) {
            System.out.println("  Sub-record: "+name);
            return null;
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

        }
    }
}
