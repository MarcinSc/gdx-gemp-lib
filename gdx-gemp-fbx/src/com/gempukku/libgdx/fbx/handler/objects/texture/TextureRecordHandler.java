package com.gempukku.libgdx.fbx.handler.objects.texture;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.GatherValuesRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class TextureRecordHandler extends GenericRecordHandler  implements FbxRecordHandler, Function<String, FbxRecordHandler> {
    private String type;
    private long version;
    private String textureName;
    private Array<FbxProperty> properties = new Array<>();
    private String media;
    private String fileName;
    private String relativeFilename;
    private Vector2 modelUvTranslation;
    private Vector2 modelUvScaling;
    private FbxTextureAlphaSource textureAlphaSource;
    private FloatArray cropping;

    private Array<FbxTexture> textures = new Array<>();

    public TextureRecordHandler() {
        super("Texture");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("Texture"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        switch (name) {
            case "Type":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        type = valueString;
                    }
                };
            case "Version":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueInt(int valueInt) {
                        version = valueInt;
                    }
                };
            case "TextureName":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        textureName = valueString;
                    }
                };
            case "Properties70":
                return new PropertiesRecordHandler(new Consumer<Array<FbxProperty>>() {
                    @Override
                    public void consume(Array<FbxProperty> fbxProperties) {
                        properties.addAll(fbxProperties);
                    }
                });
            case "Media":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        media = valueString;
                    }
                };
            case "FileName":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        fileName = valueString;
                    }
                };
            case "RelativeFilename":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        relativeFilename = valueString;
                    }
                };
            case "ModelUVTranslation":
                return new GatherValuesRecordHandler() {
                    @Override
                    public void endOfRecord() {
                        modelUvTranslation = new Vector2(getFloat(0), getFloat(1));
                    }
                };
            case "ModelUVScaling":
                return new GatherValuesRecordHandler() {
                    @Override
                    public void endOfRecord() {
                        modelUvScaling = new Vector2(getFloat(0), getFloat(1));
                    }
                };
            case "Texture_Alpha_Source":
                return new FbxTextureAlphaSourceRecordHandler(new Consumer<FbxTextureAlphaSource>() {
                    @Override
                    public void consume(FbxTextureAlphaSource fbxTextureAlphaSource) {
                        textureAlphaSource = fbxTextureAlphaSource;
                    }
                });
            case "Cropping":
                return new GatherValuesRecordHandler() {
                    @Override
                    public void endOfRecord() {
                        cropping = new FloatArray();
                        cropping.add(getFloat(0), getFloat(1), getFloat(2), getFloat(3));
                    }
                };
        }
        return super.newRecord(name);
    }

    @Override
    public void endOfRecord() {
        super.endOfRecord();

        textures.add(new FbxTexture(values, type, version, textureName, properties, media, fileName, relativeFilename, modelUvTranslation, modelUvScaling,
                textureAlphaSource, cropping));
        values = new Array<>();
        type = null;
        version = 0;
        textureName = null;
        properties = new Array<>();
        media = null;
        fileName = null;
        relativeFilename = null;
        modelUvTranslation = null;
        modelUvScaling = null;
        textureAlphaSource = null;
        cropping = null;
    }

    public Array<FbxTexture> getTextures() {
        return textures;
    }
}
