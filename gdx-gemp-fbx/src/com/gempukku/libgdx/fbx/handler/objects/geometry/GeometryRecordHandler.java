package com.gempukku.libgdx.fbx.handler.objects.geometry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.*;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.FloatArrayRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.IntArrayRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.MappingInformationType;
import com.gempukku.libgdx.fbx.handler.objects.geometry.element.ReferenceInformationType;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class GeometryRecordHandler extends GenericRecordHandler implements FbxRecordHandler, Function<String, FbxRecordHandler> {
    private Array<FbxGeometry> geometries = new Array<>();

    private FloatArray vertices;
    private IntArray polygonVertexIndex;
    private Array<IndexedFloatData> normalsData = new Array<>();
    private Array<IndexedFloatData> uvsData = new Array<>();
    private Array<IndexedData> materialsData = new Array<>();
    private Array<FbxProperty> properties = new Array<>();

    public GeometryRecordHandler() {
        super("Geometry");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("Geometry"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        switch (name) {
            case "Properties70":
                return new PropertiesRecordHandler(new Consumer<Array<FbxProperty>>() {
                    @Override
                    public void consume(Array<FbxProperty> fbxProperties) {
                        properties.addAll(fbxProperties);
                    }
                });
            case "Vertices":
                return new FloatArrayRecordHandler(
                        new Consumer<FloatArray>() {
                            @Override
                            public void consume(FloatArray floatArray) {
                                vertices = floatArray;
                            }
                        });
            case "PolygonVertexIndex":
                return new IntArrayRecordHandler(
                        new Consumer<IntArray>() {
                            @Override
                            public void consume(IntArray intArray) {
                                polygonVertexIndex = intArray;
                            }
                        });
            case "Normals":
                return new FloatArrayRecordHandler(
                        new Consumer<FloatArray>() {
                            @Override
                            public void consume(FloatArray floatArray) {
                                normalsData.add(new IndexedFloatData(0, "", floatArray, null, MappingInformationType.ByPolygonVertex, ReferenceInformationType.Direct));
                            }
                        });
            case "UV":
                return new FloatArrayRecordHandler(
                        new Consumer<FloatArray>() {
                            @Override
                            public void consume(FloatArray floatArray) {
                                uvsData.add(new IndexedFloatData(0, "", floatArray, null, MappingInformationType.ByPolygonVertex, ReferenceInformationType.Direct));
                            }
                        });
            case "LayerElementNormal":
                return new IndexedFloatDataRecordHandler(new Consumer<IndexedFloatData>() {
                    @Override
                    public void consume(IndexedFloatData indexedFloatData) {
                        normalsData.add(indexedFloatData);
                    }
                }, "Normals", "NormalsIndex");
            case "LayerElementUV":
                return new IndexedFloatDataRecordHandler(new Consumer<IndexedFloatData>() {
                    @Override
                    public void consume(IndexedFloatData indexedFloatData) {
                        uvsData.add(indexedFloatData);
                    }
                }, "UV", "UVIndex");
            case "LayerElementMaterial":
                return new IndexedDataRecordHandler(new Consumer<IndexedData>() {
                    @Override
                    public void consume(IndexedData indexedData) {
                        materialsData.add(indexedData);
                    }
                }, "Materials");
        }
        return super.newRecord(name);
    }

    @Override
    public void endOfRecord() {
        super.endOfRecord();

        geometries.add(new FbxGeometry(values, vertices, polygonVertexIndex, normalsData, uvsData, materialsData));
        values = new Array<>();
        vertices = null;
        polygonVertexIndex = null;
        normalsData = new Array<>();
        uvsData = new Array<>();
        materialsData = new Array<>();
        properties = new Array<>();
    }

    public Array<FbxGeometry> getGeometries() {
        return geometries;
    }
}
