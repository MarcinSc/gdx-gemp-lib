package com.gempukku.libgdx.test.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.test.component.AnchorComponent;
import com.gempukku.libgdx.test.component.PositionComponent;
import com.gempukku.libgdx.test.component.SizeComponent;
import com.gempukku.libgdx.test.component.SpriteComponent;

public class SimpleSprite implements Sprite {
    private Entity entity;
    private TextureRegion textureRegion;
    private boolean textureDirty = true;

    public SimpleSprite(Entity entity, TextureRegion textureRegion) {
        this.entity = entity;
        this.textureRegion = textureRegion;
    }

    @Override
    public void updateSprite(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        boolean positionDirty = positionComponent.isDirty();

        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        if (positionDirty) {
            final SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
            final AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);

            graphSprites.updateSprite(spriteComponent.getGraphSprite(),
                    new SpriteUpdater() {
                        @Override
                        public float processUpdate(float layer, Vector2 position, Vector2 size, Vector2 anchor) {
                            positionComponent.getPosition(position);
                            sizeComponent.getSize(size);
                            anchorComponent.getAnchor(anchor);
                            return layer;
                        }
                    });

            positionComponent.clean();
        }
        if (textureDirty) {
            graphSprites.setProperty(spriteComponent.getGraphSprite(), "Texture", textureRegion);
        }

        textureDirty = false;
    }
}
