package com.gempukku.libgdx.lib.artemis.texture;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.evaluate.PropertyEvaluator;

public class TextureSystem extends BaseSystem implements PropertyEvaluator {
    private EvaluatePropertySystem evaluatePropertySystem;

    private TextureHandler defaultTextureHandler;
    private final ObjectMap<String, TextureHandler> configuredTextureHandler = new ObjectMap<>();

    @Override
    protected void initialize() {
        evaluatePropertySystem.addPropertyEvaluator(this);
    }

    public void setDefaultTextureHandler(TextureHandler defaultTextureHandler) {
        this.defaultTextureHandler = defaultTextureHandler;
    }

    public void addTextureHandler(String atlas, TextureHandler textureHandler) {
        configuredTextureHandler.put(atlas, textureHandler);
    }

    public TextureRegion getTextureRegion(String atlas, String region) {
        TextureHandler textureHandler = configuredTextureHandler.get(atlas);
        if (textureHandler == null)
            textureHandler = defaultTextureHandler;
        TextureRegion result = textureHandler.getTextureRegion(atlas, region);
        if (result == null)
            throw new GdxRuntimeException("Unable to resolve texture: " + atlas + ", " + region);
        return result;
    }

    @Override
    public boolean evaluatesProperty(Entity entity, EvaluableProperty value) {
        return value instanceof TextureReference;
    }

    @Override
    public Object evaluateValue(Entity entity, EvaluableProperty value) {
        TextureReference textureReference = (TextureReference) value;
        return getTextureRegion(textureReference.getAtlas(), textureReference.getRegion());
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected void dispose() {
        if (defaultTextureHandler != null)
            defaultTextureHandler.dispose();
        for (TextureHandler textureHandler : configuredTextureHandler.values()) {
            textureHandler.dispose();
        }

        defaultTextureHandler = null;
        configuredTextureHandler.clear();
    }
}
