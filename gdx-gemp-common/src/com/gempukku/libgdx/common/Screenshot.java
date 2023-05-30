package com.gempukku.libgdx.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Screenshot {
    public static void savePixmap(Pixmap pixmap, FileHandle fileHandle) {
        PixmapIO.writePNG(fileHandle, pixmap);
    }

    public static void saveFrameBuffer(FrameBuffer frameBuffer, FileHandle fileHandle) {
        frameBuffer.begin();
        saveBoundFrameBuffer(fileHandle);
        frameBuffer.end();
    }

    public static void saveFrameBuffer(FrameBuffer frameBuffer, FileHandle fileHandle, int x, int y, int width, int height) {
        frameBuffer.begin();
        saveBoundFrameBuffer(fileHandle, x, y, width, height);
        frameBuffer.end();
    }

    public static void saveBoundFrameBuffer(FileHandle fileHandle) {
        saveBoundFrameBuffer(fileHandle, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static void saveBoundFrameBuffer(FileHandle fileHandle, int x, int y, int width, int height) {
        Pixmap pixmap = Pixmap.createFromFrameBuffer(x, y, width, height);
        try {
            savePixmap(pixmap, fileHandle);
        } finally {
            pixmap.dispose();
        }
    }

    public static void savePixmap(Pixmap pixmap, FileHandle fileHandle, int x, int y, int width, int height) {
        Pixmap regionPixmap = new Pixmap(width, height, pixmap.getFormat());
        try {
            regionPixmap.drawPixmap(pixmap, 0, 0, x, y, width, height);
            savePixmap(regionPixmap, fileHandle);
        } finally {
            regionPixmap.dispose();
        }
    }

    public static void saveTexture(Texture texture, FileHandle fileHandle) {
        TextureData textureData = texture.getTextureData();
        if (!textureData.isPrepared())
            textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();
        try {
            savePixmap(pixmap, fileHandle);
        } finally {
            pixmap.dispose();
        }
    }

    public static void saveTexture(Texture texture, FileHandle fileHandle, int x, int y, int width, int height) {
        TextureData textureData = texture.getTextureData();
        if (!textureData.isPrepared())
            textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();
        try {
            savePixmap(pixmap, fileHandle, x, y, width, height);
        } finally {
            pixmap.dispose();
        }
    }

    public static void saveTextureRegion(TextureRegion textureRegion, FileHandle fileHandle) {
        saveTexture(textureRegion.getTexture(), fileHandle, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }
}
