package com.gempukku.libgdx.ui.atlas;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UnpackTextureAtlas {
    public static void unpackTextureAtlas(FileHandle textureAtlasFile, File unpackDirectory, boolean flip) throws IOException {
        unpackDirectory.mkdirs();
        TextureAtlas.TextureAtlasData textureAtlasData = new TextureAtlas.TextureAtlasData(textureAtlasFile, textureAtlasFile.parent(), flip);
        ObjectMap<TextureAtlas.TextureAtlasData.Page, BufferedImage> pageImages = new ObjectMap<>();
        for (TextureAtlas.TextureAtlasData.Page page : textureAtlasData.getPages()) {
            File file = page.textureFile.file().getAbsoluteFile();
            pageImages.put(page, ImageIO.read(file));
        }

        for (TextureAtlas.TextureAtlasData.Region region : textureAtlasData.getRegions()) {
            String fileName = region.name + ".png";
            File imageFile = new File(unpackDirectory, fileName);
            BufferedImage pageImage = pageImages.get(region.page);
            BufferedImage unpackedImage = new BufferedImage(region.originalWidth, region.originalHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = (Graphics2D) unpackedImage.getGraphics();
            try {
                graphics.drawImage(pageImage,
                        MathUtils.round(region.offsetX), MathUtils.round(region.offsetY),
                        MathUtils.round(region.offsetX) + region.width, MathUtils.round(region.offsetY) + region.height,
                        region.left, region.top, region.left + region.width, region.top + region.height, null);
            } finally {
                graphics.dispose();
            }
            ImageIO.write(unpackedImage, "png", imageFile);
        }
    }
}
