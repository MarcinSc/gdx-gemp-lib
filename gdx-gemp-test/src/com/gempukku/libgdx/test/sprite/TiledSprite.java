package com.gempukku.libgdx.test.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.test.component.AnchorComponent;
import com.gempukku.libgdx.test.component.PositionComponent;
import com.gempukku.libgdx.test.component.SizeComponent;
import com.gempukku.libgdx.test.component.SpriteComponent;

public class TiledSprite implements Sprite {
    private Entity entity;
    private TextureRegion textureRegion;
    private Vector2 tileRepeat = new Vector2();
    private boolean textureDirty = true;

    public TiledSprite(Entity entity, TextureRegion textureRegion, Vector2 tileRepeat) {
        this.entity = entity;
        this.textureRegion = textureRegion;
        this.tileRepeat.set(tileRepeat);
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
            GraphSprite graphSprite = spriteComponent.getGraphSprite();
            graphSprites.setProperty(graphSprite, "Tile Texture", textureRegion);
            graphSprites.setProperty(graphSprite, "Tile Repeat", tileRepeat);
        }
        textureDirty = false;
    }
}
