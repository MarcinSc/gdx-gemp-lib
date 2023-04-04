package com.gempukku.libgdx.ui.tabbedpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class GTabLabel<T extends GTab> extends VisTable {
    private VisTextButton tabText;
    private VisImageButton closeButton;
    private boolean externallyModified = false;
    private GTabLabelStyle style;

    public GTabLabel(GTabControl<? super T> tabControl, T tab, String title) {
        this(tabControl, tab, "default", title);
    }

    public GTabLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title) {
        this(tabControl, tab, styleName, title, false);
    }

    public GTabLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, boolean closeable) {
        this(tabControl, tab, styleName, title, closeable, null);
    }

    public GTabLabel(GTabControl<? super T> tabControl, T tab, String styleName, String title, boolean closeable, Runnable closeRunnable) {
        this(tabControl, tab, VisUI.getSkin().get(styleName, GTabLabelStyle.class), title, closeable, closeRunnable);
    }

    public GTabLabel(GTabControl<? super T> tabControl, T tab, GTabLabelStyle style, String title, boolean closeable, Runnable closeRunnable) {
        this.style = style;

        setBackground(style.background);

        tabText = new VisTextButton(title, style.textStyle);
        tabText.getLabel().setEllipsis("...");
        tabText.getLabelCell().maxWidth(80).minWidth(1);
        tabText.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (tabText.isChecked()) {
                            tabControl.setTabActive(tab);
                        } else if (!externallyModified) {
                            tabText.setChecked(true);
                        }
                    }
                });
        tabText.setFocusBorderEnabled(false);
        tabText.setProgrammaticChangeEvents(false);
        add(tabText).maxWidth(style.textMaxWidth).minWidth(1);

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

    public void setTitle(String title) {
        tabText.setText(title);
    }

    public void setActive(boolean active) {
        tabText.setChecked(active);
        if (active) {
            if (style.backgroundActive != null)
                setBackground(style.backgroundActive);
            if (style.textStyleActive != null)
                tabText.setStyle(style.textStyleActive);
            if (closeButton != null && style.closeStyleActive != null)
                closeButton.setStyle(style.closeStyleActive);
            getCell(tabText).maxWidth(style.textMaxWidthActive);
        } else {
            setBackground(style.background);
            tabText.setStyle(style.textStyle);
            if (closeButton != null) {
                closeButton.setStyle(style.closeStyle);
            }
            getCell(tabText).maxWidth(style.textMaxWidth);
        }
    }

    public static class GTabLabelStyle {
        public int textMaxWidth = 80;
        public int textMaxWidthActive = 80;
        public Drawable background;
        /**
         * Optional
         */
        public Drawable backgroundActive;
        public VisTextButton.VisTextButtonStyle textStyle;
        /**
         * Optional
         */
        public VisTextButton.VisTextButtonStyle textStyleActive;
        public VisImageButton.VisImageButtonStyle closeStyle;
        /**
         * Optional
         */
        public VisImageButton.VisImageButtonStyle closeStyleActive;
    }
}
