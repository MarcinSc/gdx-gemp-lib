package com.gempukku.libgdx.fbx.handler;

import com.gempukku.libgdx.fbx.stream.*;

import java.io.OutputStream;

public interface FbxRecordHandler extends FbxRecordContainer {
    void propertyValueShort(short valueShort);

    void propertyValueBoolean(boolean valueBoolean);

    void propertyValueInt(int valueInt);

    void propertyValueFloat(float valueFloat);

    void propertyValueDouble(double valueDouble);

    void propertyValueLong(long valueLong);

    void propertyValueString(String valueString);

    OutputStream propertyValueRawBytes(int length);

    FloatStream propertyValueFloatArray(int arrayLength);

    DoubleStream propertyValueDoubleArray(int arrayLength);

    LongStream propertyValueLongArray(int arrayLength);

    IntStream propertyValueIntArray(int arrayLength);

    BooleanStream propertyValueBooleanArray(int arrayLength);

    void endOfRecord();
}
