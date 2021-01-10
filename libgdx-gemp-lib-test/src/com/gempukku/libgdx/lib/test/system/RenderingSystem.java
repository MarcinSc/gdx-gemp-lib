package com.gempukku.libgdx.lib.test.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.lib.test.component.SpriteComponent;
import com.gempukku.libgdx.lib.test.sprite.Sprite;
import com.gempukku.libgdx.lib.test.sprite.SpriteProducer;

public class RenderingSystem extends EntitySystem implements SpriteProducer.TextureLoader, Disposable, EntityListener {
    private TimeProvider timeProvider;
    private PipelineRenderer pipelineRenderer;
    private SpriteProducer.TextureLoader textureLoader;
    private ImmutableArray<Entity> spriteEntities;

    public RenderingSystem(int priority, TimeProvider timeProvider, PipelineRenderer pipelineRenderer,
                           SpriteProducer.TextureLoader textureLoader) {
        super(priority);
        this.timeProvider = timeProvider;
        this.pipelineRenderer = pipelineRenderer;
        this.textureLoader = textureLoader;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family sprite = Family.all(SpriteComponent.class).get();
        spriteEntities = engine.getEntitiesFor(sprite);
        engine.addEntityListener(sprite, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();
        GraphSprite graphSprite = graphSprites.createSprite(spriteComponent.getLayer());
        spriteComponent.setGraphSprite(graphSprite);

        Sprite sprite = SpriteProducer.createSprite(entity, this, graphSprite, spriteComponent);
        sprite.updateSprite(timeProvider, pipelineRenderer);
        spriteComponent.setSprite(sprite);

        for (String tag : spriteComponent.getTags()) {
            graphSprites.addTag(graphSprite, tag);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    @Override
    public void update(float delta) {
        for (Entity spriteEntity : spriteEntities) {
            SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);
            sprite.getSprite().updateSprite(timeProvider, pipelineRenderer);
        }
    }


    @Override
    public Texture loadTexture(String path) {
        return textureLoader.loadTexture(path);
    }

    @Override
    public void dispose() {
    }
}
