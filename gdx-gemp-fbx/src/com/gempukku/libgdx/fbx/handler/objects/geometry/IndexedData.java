package com.gempukku.libgdx.fbx.handler.objects.geometry;

import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.MappingInformationType;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.ReferenceInformationType;

public class IndexedData {
    private int index;
    private String name;
    private IntArray indexArray;
    private MappingInformationType mappingInformationType;
    private ReferenceInformationType referenceInformationType;

    public IndexedData(int index, String name, IntArray indexArray, MappingInformationType mappingInformationType, ReferenceInformationType referenceInformationType) {
        this.index = index;
        this.name = name;
        this.indexArray = indexArray;
        this.mappingInformationType = mappingInformationType;
        this.referenceInformationType = referenceInformationType;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public IntArray getIndexArray() {
        return indexArray;
    }

    public MappingInformationType getMappingInformationType() {
        return mappingInformationType;
    }

    public ReferenceInformationType getReferenceInformationType() {
        return referenceInformationType;
    }
}
