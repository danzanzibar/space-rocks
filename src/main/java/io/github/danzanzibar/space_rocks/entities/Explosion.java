//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents an explosion.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.graphics.OneShotAnimation;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

public class Explosion extends Entity {

    // Constants for media files.
    public static final String IMG_FILENAME = "explosion.png";
    public static final String SOUND_FILENAME = "explosion.wav";

    // Constants for the sprite sheet and animation.
    public static final int SPRITE_WIDTH = 96;
    public static final int SPRITE_HEIGHT = 93;
    public static final int NUM_SPRITES = 20;
    public static final int MILLIS_PER_FRAME = 25;

    // Constants for the entity size.
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private final OneShotAnimation animation;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Explosion(Kinematic2D kinematic, Grid grid, OneShotAnimation animation) {
        super(kinematic, animation, grid);

        this.animation = animation;
        animation.start();
    }

    //--------------------------------------------------------------------------
    //  The implementation of the parent's abstract method. Kills the explosion
    //  after the animation is done.
    //--------------------------------------------------------------------------
    @Override
    public void updateKinematic(double dt) {
        if (!animation.isRunning()) {
            kill();
            setDrawable(null);
        }
        else
            getKinematic().update(dt);
    }
}
