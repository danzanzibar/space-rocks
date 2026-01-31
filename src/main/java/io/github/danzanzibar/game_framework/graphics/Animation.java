//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents an animation. It holds a 'SpriteSheet' and data on how
//  to draw and change images.
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

import io.github.danzanzibar.game_framework.core.Updatable;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Animation implements Drawable, Updatable {
    // Stores the image data and animation rate.
    protected final SpriteSheet spriteSheet;
    private final double secondsPerFrame;

    // Stores the current image data.
    protected int imgIndex;
    protected Sprite sprite;

    // Stores the state of the animation and accumulated time.
    private boolean running;
    private double acc;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Animation(SpriteSheet spriteSheet, int spriteWidth, int spriteHeight,
                     int millisPerFrame) {
        this.spriteSheet = spriteSheet;
        this.secondsPerFrame = millisPerFrame / 1000.0;

        // Set up the sprite.
        imgIndex = 0;
        sprite = new Sprite(spriteSheet.getSubImage(imgIndex), spriteWidth, spriteHeight);

        // Set up the state and accumulator.
        running = false;
        acc = 0;
    }

    //--------------------------------------------------------------------------
    //  Get the number of sprites.
    //--------------------------------------------------------------------------
    public int getNumSprites() {
        return spriteSheet.getNumSprites();
    }

    //--------------------------------------------------------------------------
    //  Set the 'sprite' image using the index of the image from the sprite
    //  sheet.
    //--------------------------------------------------------------------------
    public void setImgIndex(int imgIndex) {

        // Check if index is in bounds.
        if (imgIndex < 0 || imgIndex >= spriteSheet.getNumSprites())
            throw new IllegalArgumentException("Invalid index");

        // Set the current image in 'sprite'.
        this.imgIndex = imgIndex;
        sprite.setImg(spriteSheet.getSubImage(imgIndex));
    }

    //--------------------------------------------------------------------------
    //  The 'Drawable' interface's method. Delegate drawing to 'sprite'.
    //--------------------------------------------------------------------------
    @Override
    public void draw(Graphics2D g, ImageObserver imageObserver) {
        sprite.draw(g, imageObserver);
    }

    //--------------------------------------------------------------------------
    //  The 'Updatable' interface's method. Handles mutating the animation based
    //  on time.
    //--------------------------------------------------------------------------
    @Override
    public void update(double dt) {

        // Return early if not running.
        if (!running)
            return;

        // Accumulate time.
        acc += dt;

        // Advance frames based on accumulated time.
        while (acc >= secondsPerFrame) {
            advanceFrame();
            acc -= secondsPerFrame;
        }
    }

    //--------------------------------------------------------------------------
    //  Start the animation.
    //--------------------------------------------------------------------------
    public void start() {
        running = true;
    }

    //--------------------------------------------------------------------------
    //  Stop the animation.
    //--------------------------------------------------------------------------
    public void stop() {
        running = false;
    }

    //--------------------------------------------------------------------------
    //  A predicate function indicating if the animation is running.
    //--------------------------------------------------------------------------
    public boolean isRunning() {
        return running;
    }

    //--------------------------------------------------------------------------
    //  Advance to the next frame of the animation.
    //--------------------------------------------------------------------------
    protected void advanceFrame() {
        imgIndex++;
        if (imgIndex == spriteSheet.getNumSprites())
            imgIndex = 0;
        sprite.setImg(spriteSheet.getSubImage(imgIndex));
    }
}
