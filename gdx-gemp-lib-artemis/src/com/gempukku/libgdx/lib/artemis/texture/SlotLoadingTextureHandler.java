package com.gempukku.libgdx.lib.artemis.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;

public class SlotLoadingTextureHandler implements TextureHandler, Disposable {
    private int pageWidth;
    private int pageHeight;
    private int cellWidth;
    private int cellHeight;
    private TextureRegion defaultTextureRegion;
    private Array<SlotLoadingPage> pages = new Array<>();
    private AssetManager assetManager;

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
        SlotLoadingPage page = findPageWithSpace();
        page.addImage(path, imageLoadNotifier);
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
        private Pixmap pixmap;
        private Texture texture;
        private ObjectMap<String, ImageLoadNotifier> notifiers = new ObjectMap<>();
        private int cellWidth;
        private int cellHeight;
        private LoadingImage[][] loadingImages;
        private ObjectMap<String, LoadingImage> loadingImageMap = new ObjectMap<>();
        private ObjectSet<LoadingImage> loading = new ObjectSet<>();
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

        public void addImage(String path, ImageLoadNotifier imageLoadNotifier) {
            assetManager.load(path, Pixmap.class);
            LoadingImage loadingImage = insertIntoCells(path, imageLoadNotifier);
            loading.add(loadingImage);
            loadingImageMap.put(path, loadingImage);
            remainingSpace--;
        }

        private LoadingImage insertIntoCells(String path, ImageLoadNotifier imageLoadNotifier) {
            LoadingImage loadingImage = null;
            for (int i = 0; i < loadingImages.length; i++) {
                for (int j = 0; j < loadingImages[i].length; j++) {
                    if (loadingImages[i][j] == null) {
                        loadingImage = new LoadingImage(path, i, j, imageLoadNotifier);
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
            private String path;
            private int cellX;
            private int cellY;
            private ImageLoadNotifier notifier;

            private TextureRegion textureRegion;

            public LoadingImage(String path, int cellX, int cellY, ImageLoadNotifier notifier) {
                this.path = path;
                this.cellX = cellX;
                this.cellY = cellY;
                this.notifier = notifier;
            }

            public boolean update() {
                if (assetManager.isLoaded(path)) {
                    Pixmap loadedPixmap = assetManager.get(path, Pixmap.class);

                    int loadedWidth = loadedPixmap.getWidth();
                    int loadedHeight = loadedPixmap.getHeight();
                    if (loadedWidth > cellWidth || loadedHeight > cellHeight) {
                        notifier.textureError();
                        return true;
                    }

                    int x = cellX * cellWidth + (cellWidth - loadedWidth) / 2;
                    int y = cellY * cellHeight + (cellHeight - loadedHeight) / 2;

                    texture.draw(loadedPixmap, x, y);
                    textureRegion = new TextureRegion(texture, x, y, loadedWidth, loadedHeight);
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
}
