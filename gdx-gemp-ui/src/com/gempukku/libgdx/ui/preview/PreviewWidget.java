package com.gempukku.libgdx.ui.preview;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.ui.DisposableWidget;
import com.kotcrab.vis.ui.VisUI;

public class PreviewWidget extends DisposableWidget {
    private final Rectangle viewRectangle = new Rectangle();
    private float viewScale = 1;
    private final NavigableCanvas navigableCanvas;
    private final ElementRenderingCallback callback = new ElementRenderingCallback();
    private boolean movedThisFrame = false;
    private final PreviewWidgetStyle style;

    public PreviewWidget(NavigableCanvas navigableCanvas) {
        this(navigableCanvas, "default");
    }

    public PreviewWidget(NavigableCanvas navigableCanvas, String styleName) {
        this(navigableCanvas, VisUI.getSkin().get(styleName, PreviewWidgetStyle.class));
    }

    public PreviewWidget(NavigableCanvas navigableCanvas, PreviewWidgetStyle style) {
        this.navigableCanvas = navigableCanvas;
        this.style = style;
        addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        navigateCanvasTo(x + getX(), y + getY());
                    }
                });
        DragListener moveCanvasDragListener = new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (event.getTarget() == PreviewWidget.this && !movedThisFrame) {
                    navigateCanvasTo(x + getX(), y + getY());
                    movedThisFrame = true;
                }
            }
        };
        moveCanvasDragListener.setTapSquareSize(0);
        addListener(moveCanvasDragListener);
    }

    @Override
    protected void initializeWidget() {

    }

    @Override
    protected void disposeWidget() {

    }

    private void navigateCanvasTo(float x, float y) {
        float currentMiddleX = viewRectangle.x + viewRectangle.width / 2;
        float currentMiddleY = viewRectangle.y + viewRectangle.height / 2;

        float difX = x - currentMiddleX;
        float difY = y - currentMiddleY;

        Vector2 tmp = new Vector2();
        navigableCanvas.getCanvasPosition(tmp);
        float canvasX = tmp.x;
        float canvasY = tmp.y;

        navigableCanvas.navigateTo(MathUtils.round(canvasX + difX / viewScale), MathUtils.round(canvasY + difY / viewScale));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float width = getWidth();
        float height = getHeight();
        float x = getX();
        float y = getY();

        Vector2 tmp = new Vector2();
        navigableCanvas.getCanvasPosition(tmp);
        float canvasX = tmp.x;
        float canvasY = tmp.y;
        navigableCanvas.getCanvasSize(tmp);
        float canvasWidth = tmp.x;
        float canvasHeight = tmp.y;
        float canvasRatio = canvasWidth / canvasHeight;
        float ratio = width / height;
        if (canvasRatio > ratio) {
            viewScale = width / canvasWidth;
        } else {
            viewScale = height / canvasHeight;
        }

        float originX = (width - canvasWidth * viewScale) / 2f;
        float originY = (height - canvasHeight * viewScale) / 2f;

        style.background.draw(batch, x, y, width, height);

        batch.flush();
        if (clipBegin(x, y, width, height)) {
            style.canvas.draw(batch, x + originX, y + originY, canvasWidth * viewScale, canvasHeight * viewScale);

            callback.prepareCallback(batch, x + originX, y + originY);
            navigableCanvas.processElements(callback);

            navigableCanvas.getVisibleSize(tmp);
            style.visible.draw(batch, x + originX + canvasX * viewScale, y + originY + canvasY * viewScale,
                    tmp.x * viewScale, tmp.y * viewScale);
            batch.flush();
            clipEnd();
        }

        viewRectangle.set(x + originX + canvasX * viewScale, y + originY + canvasY * viewScale,
                tmp.x * viewScale, tmp.y * viewScale);
        movedThisFrame = false;
    }

    private class ElementRenderingCallback implements NavigableCanvas.Callback {
        private Batch batch;
        private float x;
        private float y;

        public void prepareCallback(Batch batch, float x, float y) {
            this.batch = batch;
            this.x = x;
            this.y = y;
        }

        @Override
        public void processElement(float elementX, float elementY, float elementWidth, float elementHeight) {
            style.element.draw(batch, x + elementX * viewScale, y + elementY * viewScale,
                    elementWidth * viewScale, elementHeight * viewScale);
        }
    }

    public static class PreviewWidgetStyle {
        public Drawable background;
        public Drawable canvas;
        public Drawable element;
        public Drawable visible;
    }
}
