package com.gempukku.libgdx.lib.artemis.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;

public class SlotLoadingTextureHandler implements TextureHandler {
    private final int pageWidth;
    private final int pageHeight;
    private final int cellWidth;
    private final int cellHeight;
    private final TextureRegion defaultTextureRegion;
    private final Array<SlotLoadingPage> pages = new Array<>();
    private final AssetManager assetManager;

    private static final LoadedImageCopier defaultLoadedImageCopier = new DefaultLoadedImageCopier();

    public SlotLoadingTextureHandler(int pageWidth, int pageHeight, int cellWidth, int cellHeight,
                                     TextureRegion defaultTextureRegion) {
        this(pageWidth, pageHeight, cellWidth, cellHeight, new InternalFileHandleResolver(), defaultTextureRegion);
    }

    public SlotLoadingTextureHandler(int pageWidth, int pageHeight, int cellWidth, int cellHeight,
                                     FileHandleResolver fileHandleResolver, TextureRegion defaultTextureRegion) {
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.defaultTextureRegion = defaultTextureRegion;
        this.assetManager = new AssetManager(fileHandleResolver);
    }

    public void addImage(String path, ImageLoadNotifier imageLoadNotifier) {
        addImage(path, imageLoadNotifier, defaultLoadedImageCopier);
    }

    public void addImage(String path, ImageLoadNotifier imageLoadNotifier, LoadedImageCopier loadedImageCopier) {
        SlotLoadingPage page = findPageWithSpace();
        page.addImage(path, imageLoadNotifier, loadedImageCopier);
    }

    private SlotLoadingPage findPageWithSpace() {
        for (SlotLoadingPage page : pages) {
            if (page.hasSpace())
                return page;
        }
        SlotLoadingPage newPage = new SlotLoadingPage(pageWidth, pageHeight, cellWidth, cellHeight);
        pages.add(newPage);
        return newPage;
    }

    public void removeImage(String path) {
        for (SlotLoadingPage page : pages) {
            if (page.hasImage(path)) {
                page.removeImage(path);
                return;
            }
        }
    }

    @Override
    public TextureRegion getTextureRegion(String atlas, String region) {
        for (SlotLoadingPage page : pages) {
            TextureRegion result = page.getTextureRegion(region);
            if (result != null) {
                return result;
            }
        }
        return defaultTextureRegion;
    }

    public void update() {
        for (SlotLoadingPage page : new Array.ArrayIterable<>(pages)) {
            page.update();
        }
    }

    @Override
    public void dispose() {
        for (SlotLoadingPage page : pages) {
            page.dispose();
        }
        pages.clear();
        assetManager.dispose();
    }

    private class SlotLoadingPage implements Disposable {
        private final Pixmap pixmap;
        private final Texture texture;
        private final int cellWidth;
        private final int cellHeight;
        private final LoadingImage[][] loadingImages;
        private final ObjectMap<String, LoadingImage> loadingImageMap = new ObjectMap<>();
        private final ObjectSet<LoadingImage> loading = new ObjectSet<>();
        private int remainingSpace;

        public SlotLoadingPage(int width, int height, int cellWidth, int cellHeight) {
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            int cellXCount = MathUtils.floor(1f * width / cellWidth);
            int cellYCount = MathUtils.floor(1f * height / cellHeight);
            loadingImages = new LoadingImage[cellXCount][cellYCount];
            remainingSpace = cellXCount * cellYCount;

            pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
            pixmap.setColor(0, 0, 0, 0);
            texture = new Texture(pixmap);
        }

        public boolean hasSpace() {
            return remainingSpace > 0;
        }

        public void addImage(String path, ImageLoadNotifier imageLoadNotifier, LoadedImageCopier loadedImageCopier) {
            assetManager.load(path, Pixmap.class);
            LoadingImage loadingImage = insertIntoCells(path, imageLoadNotifier, loadedImageCopier);
            loading.add(loadingImage);
            loadingImageMap.put(path, loadingImage);
            remainingSpace--;
        }

        private LoadingImage insertIntoCells(String path, ImageLoadNotifier imageLoadNotifier, LoadedImageCopier loadedImageCopier) {
            LoadingImage loadingImage = null;
            for (int i = 0; i < loadingImages.length; i++) {
                for (int j = 0; j < loadingImages[i].length; j++) {
                    if (loadingImages[i][j] == null) {
                        loadingImage = new LoadingImage(path, i, j, imageLoadNotifier, loadedImageCopier);
                        loadingImages[i][j] = loadingImage;
                        return loadingImage;
                    }
                }
            }
            throw new GdxRuntimeException("Unable to find space for the image");
        }

        public void removeImage(String path) {
            LoadingImage loadingImage = loadingImageMap.get(path);
            if (loadingImage != null) {
                loadingImages[loadingImage.cellX][loadingImage.cellY] = null;
                loading.remove(loadingImage);
                pixmap.fillRectangle(loadingImage.cellX * cellWidth, loadingImage.cellY * cellHeight, cellWidth, cellHeight);
                remainingSpace--;
            }
        }

        public void update() {
            assetManager.update();
            ObjectSet.ObjectSetIterator<LoadingImage> loadingIterator = loading.iterator();
            while (loadingIterator.hasNext()) {
                LoadingImage loadingImage = loadingIterator.next();
                if (loadingImage.update()) {
                    loadingIterator.remove();
                }
            }
        }

        @Override
        public void dispose() {
            texture.dispose();
            pixmap.dispose();
        }

        public TextureRegion getTextureRegion(String path) {
            LoadingImage loadingImage = loadingImageMap.get(path);
            if (loadingImage != null) {
                TextureRegion textureRegion = loadingImage.getTextureRegion();
                if (textureRegion != null)
                    return textureRegion;
                return defaultTextureRegion;
            }
            return null;
        }

        public boolean hasImage(String path) {
            return loadingImageMap.containsKey(path);
        }

        private class LoadingImage {
            private final String path;
            private final int cellX;
            private final int cellY;
            private final ImageLoadNotifier notifier;

            private TextureRegion textureRegion;
            private final LoadedImageCopier loadedImageCopier;

            public LoadingImage(String path, int cellX, int cellY, ImageLoadNotifier notifier, LoadedImageCopier loadedImageCopier) {
                this.path = path;
                this.cellX = cellX;
                this.cellY = cellY;
                this.notifier = notifier;
                this.loadedImageCopier = loadedImageCopier;
            }

            public boolean update() {
                if (assetManager.isLoaded(path)) {
                    Pixmap loadedPixmap = assetManager.get(path, Pixmap.class);

                    int x = cellX * cellWidth;
                    int y = cellY & cellHeight;

                    textureRegion = loadedImageCopier.copyPixmapToTexture(loadedPixmap, texture, x, y, cellWidth, cellHeight);

                    notifier.textureLoaded(textureRegion);
                    assetManager.unload(path);

                    return true;
                }
                return false;
            }

            public TextureRegion getTextureRegion() {
                return textureRegion;
            }
        }
    }

    private static class DefaultLoadedImageCopier implements LoadedImageCopier {
        @Override
        public TextureRegion copyPixmapToTexture(Pixmap pixmap, Texture texture, int x, int y, int cellWidth, int cellHeight) {
            int loadedWidth = pixmap.getWidth();
            int loadedHeight = pixmap.getHeight();
            if (loadedWidth > cellWidth || loadedHeight > cellHeight) {
                throw new IllegalArgumentException("Loaded pixmap is too large");
            }

            int startX = x + (cellWidth - loadedWidth) / 2;
            int startY = y + (cellHeight - loadedHeight) / 2;

            texture.draw(pixmap, startX, startY);
            return new TextureRegion(texture, startX, startY, loadedWidth, loadedHeight);
        }
    }
}
