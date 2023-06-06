package com.gempukku.libgdx.fbx.handler.objects.geometry;

import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.*;

public class IndexedDataRecordHandler extends UnsupportedValueRecordHandler implements FbxRecordHandler {
    private int index;
    private String name;
    private MappingInformationType mappingInformationType = MappingInformationType.ByPolygonVertex;
    private ReferenceInformationType referenceInformationType = ReferenceInformationType.Direct;
    private IntArray indexArray;

    private final Consumer<IndexedData> indexedDataConsumer;
    private final String indexArrayName;

    public IndexedDataRecordHandler(Consumer<IndexedData> indexedDataConsumer, String indexArrayName) {
        this.indexedDataConsumer = indexedDataConsumer;
        this.indexArrayName = indexArrayName;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        if (name.equals("Name"))
            return new StringRecordHandler(new Consumer<String>() {
                @Override
                public void consume(String s) {
                    IndexedDataRecordHandler.this.name = s;
                }
            });
        else if (name.equals("MappingInformationType"))
            return new MappingInformationTypeRecordHandler(new Consumer<MappingInformationType>() {
                @Override
                public void consume(MappingInformationType mappingInformationType) {
                    IndexedDataRecordHandler.this.mappingInformationType = mappingInformationType;
                }
            });
        else if (name.equals("ReferenceInformationType"))
            return new ReferenceInformationTypeRecordHandler(new Consumer<ReferenceInformationType>() {
                @Override
                public void consume(ReferenceInformationType referenceInformationType) {
                    IndexedDataRecordHandler.this.referenceInformationType = referenceInformationType;
                }
            });
        else if (name.equals(indexArrayName))
            return new IntArrayRecordHandler(new Consumer<IntArray>() {
                @Override
                public void consume(IntArray intArray) {
                    IndexedDataRecordHandler.this.indexArray = intArray;
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
        indexedDataConsumer.consume(new IndexedData(index, name, indexArray, mappingInformationType, referenceInformationType));
    }
}
