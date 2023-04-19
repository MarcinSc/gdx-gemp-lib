package com.gempukku.libgdx.ui.tabbedpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;

public class GDirtyTabIconLabel<T extends GDirtyTab> extends Button {
    private final Image dirtyImage;
    private Image iconImage;
    private final VisLabel textLabel;
    private VisImageButton closeButton;

    private boolean active = false;
    private boolean dirty = false;

    private final T tab;
    private final GDirtyTabImageLabelStyle style;

    public GDirtyTabIconLabel(GTabControl<? super T> tabControl, T tab, String title) {
        this (tabControl, tab, "default", title);
    }

    public GDirtyTabIconLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title) {
        this(tabControl, tab, styleName, title, null, false);
    }

    public GDirtyTabIconLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, Drawable icon) {
        this(tabControl, tab, styleName, title, icon, false);
    }

    public GDirtyTabIconLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, Drawable icon,
                              boolean closeable) {
        this(tabControl, tab, styleName, title, icon, closeable, null);
    }

    public GDirtyTabIconLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, Drawable icon,
                              boolean closeable, Runnable closeRunnable) {
        this(tabControl, tab, VisUI.getSkin().get(styleName, GDirtyTabImageLabelStyle.class), title, icon, closeable, closeRunnable);
    }

    public GDirtyTabIconLabel(final GTabControl<? super T> tabControl, final T tab, GDirtyTabImageLabelStyle style,
                              String title, Drawable icon, boolean closeable, final Runnable closeRunnable) {
        this.tab = tab;
        this.style = style;

        setStyle(style.buttonStyle);

        dirtyImage = new Image(style.clean, Scaling.fit);
        add(dirtyImage);

        if (icon != null) {
            iconImage = new Image(icon, Scaling.fit);
            add(iconImage);
        }

        textLabel = new VisLabel(title, style.textStyle);
        textLabel.setEllipsis("...");
        add(textLabel).maxWidth(style.textMaxWidth).minWidth(1);

        addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (isChecked()) {
                            tabControl.setTabActive(tab);
                        } else {
                            setChecked(true);
                        }
                    }
                });
        setProgrammaticChangeEvents(false);

        if (closeable) {
            closeButton = new VisImageButton(style.closeStyle);
            closeButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (closeRunnable != null)
                                closeRunnable.run();
                            else
                                tabControl.closeTab(tab);
                        }
                    });
            closeButton.setFocusBorderEnabled(false);
            closeButton.setProgrammaticChangeEvents(false);
            add(closeButton);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        boolean tabDirty = tab.isDirty();
        if (tabDirty != dirty) {
            dirty = tabDirty;
            updateStyles();
        }
    }

    public void setTitle(String title) {
        textLabel.setText(title);
    }

    public void setActive(boolean active) {
        this.active = active;
        updateStyles();
    }

    private void updateStyles() {
        setChecked(active);
        if (active) {
            getCell(textLabel).maxWidth(style.activeTextMaxWidth).minWidth(1);
            if (style.activeTextStyle != null)
                textLabel.setStyle(style.activeTextStyle);
            if (style.activeCloseStyle != null)
                closeButton.setStyle(style.activeCloseStyle);
        } else {
            getCell(textLabel).maxWidth(style.textMaxWidth).minWidth(1);
            textLabel.setStyle(style.textStyle);
            closeButton.setStyle(style.closeStyle);
        }
        if (dirty) {
            dirtyImage.setDrawable(style.dirty);
        } else {
            dirtyImage.setDrawable(style.clean);
        }
    }

    public static class GDirtyTabImageLabelStyle {
        public ButtonStyle buttonStyle;
        public Drawable dirty;
        public Drawable clean;
        public int textMaxWidth = 80;
        public int activeTextMaxWidth = 80;
        public Label.LabelStyle textStyle;
        /**
         * Optional
         */
        public Label.LabelStyle activeTextStyle;
        public VisImageButton.VisImageButtonStyle closeStyle;
        /**
         * Optional
         */
        public VisImageButton.VisImageButtonStyle activeCloseStyle;

    }
}
