package com.gempukku.libgdx.fbx.handler.objects.geometry;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.*;

public class IndexedFloatDataRecordHandler extends UnsupportedValueRecordHandler implements FbxRecordHandler {
    private int index;
    private String name;
    private MappingInformationType mappingInformationType = MappingInformationType.ByPolygonVertex;
    private ReferenceInformationType referenceInformationType = ReferenceInformationType.Direct;
    private FloatArray floatArray;
    private IntArray indexArray;

    private final Consumer<IndexedFloatData> indexedDataConsumer;
    private final String floatArrayName;
    private final String indexArrayName;

    public IndexedFloatDataRecordHandler(Consumer<IndexedFloatData> indexedDataConsumer, String floatArrayName, String indexArrayName) {
        this.indexedDataConsumer = indexedDataConsumer;
        this.floatArrayName = floatArrayName;
        this.indexArrayName = indexArrayName;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        if (name.equals("Name"))
            return new StringRecordHandler(new Consumer<String>() {
                @Override
                public void consume(String s) {
                    IndexedFloatDataRecordHandler.this.name = s;
                }
            });
        else if (name.equals("MappingInformationType"))
            return new MappingInformationTypeRecordHandler(new Consumer<MappingInformationType>() {
                @Override
                public void consume(MappingInformationType mappingInformationType) {
                    IndexedFloatDataRecordHandler.this.mappingInformationType = mappingInformationType;
                }
            });
        else if (name.equals("ReferenceInformationType"))
            return new ReferenceInformationTypeRecordHandler(new Consumer<ReferenceInformationType>() {
                @Override
                public void consume(ReferenceInformationType referenceInformationType) {
                    IndexedFloatDataRecordHandler.this.referenceInformationType = referenceInformationType;
                }
            });
        else if (name.equals(floatArrayName))
            return new FloatArrayRecordHandler(new Consumer<FloatArray>() {
                @Override
                public void consume(FloatArray floatArray) {
                    IndexedFloatDataRecordHandler.this.floatArray = floatArray;
                }
            });
        else if (name.equals(indexArrayName))
            return new IntArrayRecordHandler(new Consumer<IntArray>() {
                @Override
                public void consume(IntArray intArray) {
                    IndexedFloatDataRecordHandler.this.indexArray = intArray;
                }
            });
        return null;
    }

    @Override
    public void propertyValueInt(int valueInt) {
        index = valueInt;
    }

    @Override
    public void endOfRecord() {
        indexedDataConsumer.consume(new IndexedFloatData(index, name, floatArray, indexArray, mappingInformationType, referenceInformationType));
    }
}
