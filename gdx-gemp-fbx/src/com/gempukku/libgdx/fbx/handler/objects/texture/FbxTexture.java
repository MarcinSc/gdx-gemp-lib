package com.gempukku.libgdx.fbx.handler.objects.texture;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;

public class FbxTexture {
    private Array<Object> values;
    private String type;
    private long version;
    private String textureName;
    private Array<FbxProperty> properties;
    private String media;
    private String fileName;
    private String relativeFilename;
    private Vector2 modelUvTranslation;
    private Vector2 modelUvScaling;
    private FbxTextureAlphaSource textureAlphaSource;
    private FloatArray cropping;

    public FbxTexture(Array<Object> values, String type, long version, String textureName, Array<FbxProperty> properties, String media, String fileName, String relativeFilename, Vector2 modelUvTranslation, Vector2 modelUvScaling, FbxTextureAlphaSource textureAlphaSource, FloatArray cropping) {
        this.values = values;
        this.type = type;
        this.version = version;
        this.textureName = textureName;
        this.properties = properties;
        this.media = media;
        this.fileName = fileName;
        this.relativeFilename = relativeFilename;
        this.modelUvTranslation = modelUvTranslation;
        this.modelUvScaling = modelUvScaling;
        this.textureAlphaSource = textureAlphaSource;
        this.cropping = cropping;
    }

    public Array<Object> getValues() {
        return values;
    }

    public String getType() {
        return type;
    }

    public long getVersion() {
        return version;
    }

    public String getTextureName() {
        return textureName;
    }

    public Array<FbxProperty> getProperties() {
        return properties;
    }

    public String getMedia() {
        return media;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRelativeFilename() {
        return relativeFilename;
    }

    public Vector2 getModelUvTranslation() {
        return modelUvTranslation;
    }

    public Vector2 getModelUvScaling() {
        return modelUvScaling;
    }

    public FbxTextureAlphaSource getTextureAlphaSource() {
        return textureAlphaSource;
    }

    public FloatArray getCropping() {
        return cropping;
    }
}
