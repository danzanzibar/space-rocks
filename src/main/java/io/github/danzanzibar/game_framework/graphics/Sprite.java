//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a static 2D sprite. 
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Sprite implements Drawable {

    // The image to draw.
    private BufferedImage img;

    // Dimensions for the image to occupy.
    private final int width;
    private final int height;

    // Which direction is forward. 
    private final double forward;

    //--------------------------------------------------------------------------
    //  A constructor for the class using a default 'forward' value.
    //--------------------------------------------------------------------------
    public Sprite(BufferedImage img, int width, int height) {
        this.img = img;
        this.width = width;
        this.height = height;
        this.forward = Math.PI * 3 / 2; // Up is default "forward".
    }

    //--------------------------------------------------------------------------
    //  A fully parametrized constructor for the class.
    //--------------------------------------------------------------------------
    public Sprite(BufferedImage img, int width, int height, double forward) {
        this.img = img;
        this.width = width;
        this.height = height;
        this.forward = forward;
    }

    //--------------------------------------------------------------------------
    //  Getter and setter for 'img'.
    //--------------------------------------------------------------------------
    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    //--------------------------------------------------------------------------
    //  Getters for the dimension fields.
    //--------------------------------------------------------------------------
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //--------------------------------------------------------------------------
    //  The 'Drawable' interface's sole method to implement. Draws the image
    //  using the 'Graphics2D' object and set's the forward direction to an
    //  orientation of zero (in the direction of the positive x-axis).
    //--------------------------------------------------------------------------
    @Override
    public void draw(Graphics2D g, ImageObserver imageObserver) {
        AffineTransform oldTransform = g.getTransform();

        // Rotate so the sprite is pointed at angle = 0 before drawing it centered on the origin.
        g.rotate(-forward);
        g.drawImage(getImg(), -width / 2, -height / 2, width, height, imageObserver);
        g.setTransform(oldTransform);
    }
}
