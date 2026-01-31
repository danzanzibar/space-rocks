//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a sprite sheet.
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    // The image of the sheet.
    private final BufferedImage sheetImage;

    // The size of the sprites in pixels on the sheet.
    private final int subImgWidth;
    private final int subImgHeight;

    // The number of sprites on the sheet.
    private final int numSprites;

    // The number of columns of sprites on the sheet.
    private final int cols;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public SpriteSheet(BufferedImage sheetImage, int subImgWidth, int subImgHeight, int numSprites) {
        this.sheetImage = sheetImage;
        this.subImgWidth = subImgWidth;
        this.subImgHeight = subImgHeight;
        this.numSprites = numSprites;

        // calculate the number of columns.
        cols = sheetImage.getWidth() / subImgWidth;
    }

    //--------------------------------------------------------------------------
    //  A getter for 'numSprites'.
    //--------------------------------------------------------------------------
    public int getNumSprites() {
        return numSprites;
    }

    //--------------------------------------------------------------------------
    //  Gets an image at a given index. All images on the sheet get given an
    //  index to effectively turn them into a one-dimensional array.
    //--------------------------------------------------------------------------
    public BufferedImage getSubImage(int index) {
        if (index < 0 || index >= numSprites)
            throw new IllegalArgumentException("Index " + index + " is out of bounds");

        // Calculate the col and row.
        int col = index % cols;
        int row = index / cols;

        // Calculate the pixel offset to find the image.
        int xOffset = col * subImgWidth;
        int yOffset = row * subImgHeight;

        // Return the sub image.
        return sheetImage.getSubimage(xOffset, yOffset, subImgWidth, subImgHeight);
    }
}
