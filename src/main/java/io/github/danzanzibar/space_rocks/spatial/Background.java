//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a space background that moves with parallax to the
//  player, asteroids, and UFOs. So it gives a feeling of depth which really
//  adds to being in space in a 2D game.
//******************************************************************************
package io.github.danzanzibar.space_rocks.spatial;

import io.github.danzanzibar.game_framework.core.ResourceLoader;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.graphics.Drawable;
import io.github.danzanzibar.space_rocks.SpaceRocksApp;
import io.github.danzanzibar.space_rocks.SpaceRocksWorld;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Background implements Drawable {
    public static final String IMG_FILENAME = "nebula.png";
    public static final double IMAGE_SCALE = 1.5;
    public static final double MOVEMENT_SCALE = 3;
    public static final int TILE_DENSITY = 1;

    private final BufferedImage img;
    private final Dimension imgDims;

    private Grid grid;

    //--------------------------------------------------------------------------
    //  A constructor for the class that stored the background image to use.
    //--------------------------------------------------------------------------
    public Background() {
        img = ResourceLoader.loadImage(IMG_FILENAME);
        int img_width = (int) (img.getWidth() * IMAGE_SCALE);
        int img_height = (int) (img.getHeight() * IMAGE_SCALE);
        imgDims = new Dimension(img_width, img_height);
    }

    //--------------------------------------------------------------------------
    //  Sets the 'Grid' object. Must be set before drawing.
    //--------------------------------------------------------------------------
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    //--------------------------------------------------------------------------
    //  Returns the user space dimensions of the game. Since the background
    //  is drawn in a continuous way while the world is toroidal in nature, it
    //  is easiest if the background fits in some whole number multiple into
    //  the space. Hence, we allow the background to generate the space
    //  dimensions.
    //--------------------------------------------------------------------------
    public Dimension getUserSpaceDims() {

        // Calculate the area the background will cover based on movement scale and tile density.
        int totalWidth = (int) (imgDims.width * MOVEMENT_SCALE * TILE_DENSITY);
        int totalHeight = (int) (imgDims.height * MOVEMENT_SCALE * TILE_DENSITY);

        return new Dimension(totalWidth, totalHeight);
    }

    //--------------------------------------------------------------------------
    //  Draws the background.
    //--------------------------------------------------------------------------
    public void draw(Graphics2D g, ImageObserver imageObserver) {

        // Make sure the grid has been set.
        if (grid == null)
            throw new IllegalStateException("grid is null");

        // Calculate the offset of the background image based on the player position. This is scaled to give the
        // appearance of parallax. We start with calculating a starting corner to draw the tiled background that
        // is left and above of the visible area in user space.
        Vector2 playerPos = grid.getPlayerPos();
        int xOffset = (int) ( (-playerPos.getX() / MOVEMENT_SCALE) % imgDims.width);
        int yOffset = (int) ( (-playerPos.getY() / MOVEMENT_SCALE) % imgDims.height);

        int visualWidth = SpaceRocksWorld.VISIBLE_WIDTH;
        int visualHeight = SpaceRocksWorld.VISIBLE_HEIGHT;

        int xStart = (int) playerPos.getX() - visualWidth / 2 + xOffset;
        int yStart = (int) playerPos.getY() - visualHeight / 2 + yOffset;

        // Add tiles of the image to cover the entire visible area.
        int x = xStart;
        int y = yStart;
        while (y < yStart + visualHeight + imgDims.height) {
            while (x < xStart + visualWidth + imgDims.width) {
                g.drawImage(img, x, y, imgDims.width, imgDims.height, imageObserver);
                x += imgDims.width;
            }
            y += imgDims.height;
            x = xStart;
        }
    }
}
