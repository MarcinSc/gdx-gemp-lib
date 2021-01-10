package com.gempukku.libgdx.lib.test.component;

public class OutlineComponent extends DirtyComponent {
    private String type;
    private boolean outline;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean hasOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        if (this.outline != outline) {
            this.outline = outline;
            setDirty();
        }
    }
}
