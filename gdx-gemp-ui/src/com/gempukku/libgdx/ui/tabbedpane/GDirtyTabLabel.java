package com.gempukku.libgdx.ui.tabbedpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisTable;

public class GDirtyTabLabel<T extends GDirtyTab> extends VisTable {
    private final VisImageTextButton tabText;
    private VisImageButton closeButton;

    private boolean active = false;
    private boolean dirty = false;

    private final T tab;
    private final GDirtyTabLabelStyle style;

    public GDirtyTabLabel(GTabControl<? super T> tabControl, T tab, String title) {
        this(tabControl, tab, "default", title);
    }

    public GDirtyTabLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title) {
        this(tabControl, tab, styleName, title, false);
    }

    public GDirtyTabLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, boolean closeable) {
        this(tabControl, tab, styleName, title, closeable, null);
    }

    public GDirtyTabLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, boolean closeable, Runnable closeRunnable) {
        this(tabControl, tab, VisUI.getSkin().get(styleName, GDirtyTabLabelStyle.class), title, closeable, closeRunnable);
    }

    public GDirtyTabLabel(final GTabControl<? super T> tabControl, final T tab, GDirtyTabLabelStyle style, String title, boolean closeable, final Runnable closeRunnable) {
        this.tab = tab;
        this.style = style;

        setBackground(style.background);

        tabText = new VisImageTextButton(title, style.textStyle);
        tabText.getLabel().setEllipsis("...");
        tabText.getLabelCell().maxWidth(style.textMaxWidth).minWidth(1);
        tabText.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (tabText.isChecked()) {
                            tabControl.setTabActive(tab);
                        } else {
                            tabText.setChecked(true);
                        }
                    }
                });
        tabText.setFocusBorderEnabled(false);
        tabText.setProgrammaticChangeEvents(false);
        add(tabText);

        if (closeable) {
            closeButton = new VisImageButton(style.closeStyle);
            closeButton.addListener(
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
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
            setDirty(tabDirty);
        }
    }

    public void setTitle(String title) {
        tabText.setText(title);
    }

    public void setActive(boolean active) {
        this.active = active;
        updateStyles();
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        updateStyles();
    }

    private void updateStyles() {
        tabText.setChecked(active);
        if (active) {
            if (style.backgroundActive != null)
                setBackground(style.backgroundActive);
            if (dirty) {
                if (style.dirtyTextStyleActive != null)
                    tabText.setStyle(style.dirtyTextStyleActive);
                else
                    tabText.setStyle(style.dirtyTextStyle);
            } else {
                if (style.textStyleActive != null)
                    tabText.setStyle(style.textStyleActive);
                else
                    tabText.setStyle(style.textStyle);
            }
            if (closeButton != null && style.closeStyleActive != null)
                closeButton.setStyle(style.closeStyleActive);
            getCell(tabText).maxWidth(style.textMaxWidthActive);
        } else {
            setBackground(style.background);
            if (dirty)
                tabText.setStyle(style.dirtyTextStyle);
            else
                tabText.setStyle(style.textStyle);
            if (closeButton != null) {
                closeButton.setStyle(style.closeStyle);
            }
            getCell(tabText).maxWidth(style.textMaxWidth);
        }
    }

    public static class GDirtyTabLabelStyle extends GTabLabel.GTabLabelStyle {
        public int textMaxWidth = 80;
        public int textMaxWidthActive = 80;

        public Drawable background;
        /**
         * Optional
         */
        public Drawable backgroundActive;
        public VisImageTextButton.VisImageTextButtonStyle textStyle;
        /**
         * Optional
         */
        public VisImageTextButton.VisImageTextButtonStyle textStyleActive;
        public VisImageTextButton.VisImageTextButtonStyle dirtyTextStyle;
        /**
         * Optional
         */
        public VisImageTextButton.VisImageTextButtonStyle dirtyTextStyleActive;
        public VisImageButton.VisImageButtonStyle closeStyle;
        /**
         * Optional
         */
        public VisImageButton.VisImageButtonStyle closeStyleActive;
    }
}
