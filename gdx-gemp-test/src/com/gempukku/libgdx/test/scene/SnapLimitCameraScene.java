package com.gempukku.libgdx.test.scene;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.camera2d.Camera2DController;
import com.gempukku.libgdx.camera2d.UpdateCameraControl;
import com.gempukku.libgdx.camera2d.constraint.LockedToWindowCamera2DConstraint;
import com.gempukku.libgdx.camera2d.constraint.SceneCamera2DConstraint;
import com.gempukku.libgdx.camera2d.constraint.SnapToWindowCamera2DConstraint;
import com.gempukku.libgdx.camera2d.focus.EntityFocus;
import com.gempukku.libgdx.camera2d.focus.PositionProvider;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffect;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.shader.particles.generator.ParallelogramParticleGenerator;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.test.LibgdxLibTestScene;
import com.gempukku.libgdx.test.component.AnchorComponent;
import com.gempukku.libgdx.test.component.FacingComponent;
import com.gempukku.libgdx.test.component.PositionComponent;
import com.gempukku.libgdx.test.component.SizeComponent;
import com.gempukku.libgdx.test.entity.EntityLoader;
import com.gempukku.libgdx.test.sprite.SpriteFaceDirection;
import com.gempukku.libgdx.test.system.*;
import com.gempukku.libgdx.test.system.sensor.FootSensorContactListener;
import com.gempukku.libgdx.test.system.sensor.InteractSensorContactListener;

import java.io.IOException;
import java.io.InputStream;

public class SnapLimitCameraScene implements LibgdxLibTestScene {
    private Array<Disposable> resources = new Array<>();
    private PipelineRenderer pipelineRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private TimeKeeper timeKeeper = new DefaultTimeKeeper();

    private Engine engine;
    private TextureHolder textureHolder;
    private ShapeRenderer shapeRenderer;

    private boolean debugRender = false;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 tmpMatrix;
    private int cameraScale = 3;
    private Rectangle snapRectangle;
    private Rectangle lockRectangle;
    private PositionProvider positionProvider;

    @Override
    public void initializeScene() {
        Box2D.init();

        skin = createSkin();
        resources.add(skin);

        stage = createStage(skin);
        resources.add(stage);

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        resources.add(pipelineRenderer);

        textureHolder = new TextureHolder();

        shapeRenderer = new ShapeRenderer();
        resources.add(shapeRenderer);

        createSystems();

        Json json = new Json();

        loadEnvironment(json);

        final Entity playerEntity = EntityLoader.readEntity(engine, json, "sprite/playerBlueWizard.json");
        engine.getSystem(PlayerControlSystem.class).setPlayerEntity(playerEntity);

        positionProvider = new PositionProvider() {
            @Override
            public Vector2 getPosition(Vector2 position) {
                PositionComponent positionComponent = playerEntity.getComponent(PositionComponent.class);
                SizeComponent sizeComponent = playerEntity.getComponent(SizeComponent.class);
                AnchorComponent anchorComponent = playerEntity.getComponent(AnchorComponent.class);
                FacingComponent facingComponent = playerEntity.getComponent(FacingComponent.class);
                SpriteFaceDirection faceDirection = facingComponent.getFaceDirection();

                Vector2 anchorPos = positionComponent.getPosition(position);
                float x = anchorPos.x;
                float y = anchorPos.y;
                Vector2 anchor = anchorComponent.getAnchor(position);
                float anchorX = anchor.x;
                float anchorY = anchor.y;
                Vector2 size = sizeComponent.getSize(position);
                return position.set(x + (anchorX - 0.5f) * size.x + faceDirection.getX() * 200, y + (anchorY - 0.5f) * size.y + faceDirection.getY() * 200);
            }
        };

        snapRectangle = new Rectangle(0.4f, 0.25f, 0.2f, 0.3f);
        lockRectangle = new Rectangle(0.25f, 0.2f, 0.5f, 0.5f);

        UpdateCameraControl updateCameraControl = new UpdateCameraControl(camera);

        Camera2DController cameraController = new Camera2DController(camera,
                // Try to focus on the point provided by position provider
                new EntityFocus(positionProvider),
                new SnapToWindowCamera2DConstraint(updateCameraControl, snapRectangle, new Vector2(0.1f, 0.1f)),
                new LockedToWindowCamera2DConstraint(updateCameraControl, lockRectangle),
                // Move the camera to make sure that pixels outside of the scene bounds are not shown
                new SceneCamera2DConstraint(updateCameraControl, new Rectangle(-2560, -414, 5120, 2000)));

        engine.getSystem(CameraSystem.class).setConstraintCameraController(cameraController);

        Gdx.input.setInputProcessor(stage);

        if (debugRender) {
            debugRenderer = new Box2DDebugRenderer();
            tmpMatrix = new Matrix4();
        }
    }

    private void loadEnvironment(Json json) {
        EntityLoader.readEntity(engine, json, "sprite/ground.json");
        EntityLoader.readEntity(engine, json, "sprite/hill1.json");
        EntityLoader.readEntity(engine, json, "sprite/hill2.json");
        EntityLoader.readEntity(engine, json, "sprite/hill3.json");
        EntityLoader.readEntity(engine, json, "sprite/hill4.json");
        EntityLoader.readEntity(engine, json, "sprite/platform1.json");
        EntityLoader.readEntity(engine, json, "sprite/platform2.json");

        GraphParticleEffects particleEffects = pipelineRenderer.getGraphParticleEffects();
        ParallelogramParticleGenerator particleGenerator = new ParallelogramParticleGenerator(5);
        particleGenerator.getOrigin().set(0, 0, -10);
        particleGenerator.getDirection1().set(1, 0, 0);
        particleGenerator.getDirection2().set(0, 1, 0);
        GraphParticleEffect dustEffect = particleEffects.createEffect("Dust", particleGenerator);
        particleEffects.startEffect(dustEffect);
    }

    private void createSystems() {
        engine = new Engine();

        engine.addSystem(new PlayerControlSystem(20));

        PhysicsSystem physicsSystem = new PhysicsSystem(30, -30f);
        physicsSystem.addSensorContactListener("foot", new FootSensorContactListener());
        physicsSystem.addSensorContactListener("interact", new InteractSensorContactListener());
        engine.addSystem(physicsSystem);

        engine.addSystem(new OutlineSystem(35, pipelineRenderer));

        engine.addSystem(new RenderingSystem(40, timeKeeper, pipelineRenderer, textureHolder));

        engine.addSystem(new CameraSystem(50));
    }


    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() * cameraScale, Gdx.graphics.getHeight() * cameraScale);
        camera.position.set(0, 0, 0);
        camera.update();

        return camera;
    }

    private Skin createSkin() {
        return new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
    }

    private Stage createStage(Skin skin) {
        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapeRenderer.updateMatrices();
        camera.viewportWidth = width * cameraScale;
        camera.viewportHeight = height * cameraScale;
        camera.update();
    }

    @Override
    public void renderScene() {
        float delta = Math.min(0.03f, Gdx.graphics.getDeltaTime());
        timeKeeper.updateTime(delta);
        stage.act(delta);

        engine.update(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawCrosshair(Color.WHITE, shapeRenderer, positionProvider);
        drawRect(Color.WHITE, shapeRenderer, snapRectangle);
        drawRect(Color.RED, shapeRenderer, lockRectangle);
        shapeRenderer.end();

        if (debugRender) {
            tmpMatrix.set(camera.combined).scale(PhysicsSystem.PIXELS_TO_METERS, PhysicsSystem.PIXELS_TO_METERS, 0);
            debugRenderer.render(engine.getSystem(PhysicsSystem.class).getWorld(), tmpMatrix);
        }
    }

    private void drawCrosshair(Color color, ShapeRenderer shapeRenderer, PositionProvider position) {
        shapeRenderer.setColor(color);
        Vector2 crosshair = position.getPosition(new Vector2());

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float x = width / 2 + (crosshair.x - camera.position.x) / (camera.viewportWidth / width);
        float y = height / 2 + (crosshair.y - camera.position.y) / (camera.viewportHeight / height);

        shapeRenderer.line(x - 5, y, x + 5, y);
        shapeRenderer.line(x, y - 5, x, y + 5);
    }

    private void drawRect(Color color, ShapeRenderer shapeRenderer, Rectangle rectangle) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        shapeRenderer.setColor(color);
        shapeRenderer.rect(rectangle.x * width, rectangle.y * height, rectangle.width * width, rectangle.height * height);
    }

    @Override
    public void disposeScene() {
        for (EntitySystem system : engine.getSystems()) {
            if (system instanceof Disposable)
                ((Disposable) system).dispose();
        }

        for (Disposable resource : resources) {
            resource.dispose();
        }
        resources.clear();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.classpath("pipeline/test.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback(timeKeeper));
                setupPipeline(pipelineRenderer);
                return pipelineRenderer;
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load pipeline", exp);
        }
    }

    private void setupPipeline(PipelineRenderer pipelineRenderer) {
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.setPipelineProperty("Stage", stage);
    }
}