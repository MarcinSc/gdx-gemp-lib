package com.gempukku.libgdx.fbx.handler.objects.geometry;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.MappingInformationType;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.ReferenceInformationType;

public class IndexedFloatData {
    private int index;
    private String name;
    private FloatArray floatData;
    private IntArray floatDataIndex;
    private MappingInformationType mappingInformationType;
    private ReferenceInformationType referenceInformationType;

    public IndexedFloatData(int index, String name, FloatArray floatData, IntArray floatDataIndex, MappingInformationType mappingInformationType, ReferenceInformationType referenceInformationType) {
        this.index = index;
        this.name = name;
        this.floatData = floatData;
        this.floatDataIndex = floatDataIndex;
        this.mappingInformationType = mappingInformationType;
        this.referenceInformationType = referenceInformationType;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public FloatArray getFloatData() {
        return floatData;
    }

    public IntArray getFloatDataIndex() {
        return floatDataIndex;
    }

    public MappingInformationType getMappingInformationType() {
        return mappingInformationType;
    }

    public ReferenceInformationType getReferenceInformationType() {
        return referenceInformationType;
    }
}
