package com.gempukku.libgdx.fbx.handler.objects.geometry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;

public class FbxGeometry {
    private Array<Object> values;
    private FloatArray vertices;
    private IntArray polygonVertexIndex;
    private Array<IndexedFloatData> normalsData;
    private Array<IndexedFloatData> uvsData;
    private Array<IndexedData> materialsData;

    public FbxGeometry(Array<Object> values, FloatArray vertices, IntArray polygonVertexIndex, Array<IndexedFloatData> normalsData, Array<IndexedFloatData> uvsData, Array<IndexedData> materialsData) {
        this.values = values;
        this.vertices = vertices;
        this.polygonVertexIndex = polygonVertexIndex;
        this.normalsData = normalsData;
        this.uvsData = uvsData;
        this.materialsData = materialsData;
    }

    public Array<Object> getValues() {
        return values;
    }

    public FloatArray getVertices() {
        return vertices;
    }

    public IntArray getPolygonVertexIndex() {
        return polygonVertexIndex;
    }

    public Array<IndexedFloatData> getNormalsData() {
        return normalsData;
    }

    public Array<IndexedFloatData> getUvsData() {
        return uvsData;
    }

    public Array<IndexedData> getMaterialsData() {
        return materialsData;
    }
}
