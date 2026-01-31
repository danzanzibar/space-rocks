//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents an asteroid.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.graphics.Animation;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

public class Asteroid extends CollisionEntity {

    // Constants for the sprite sheet and animation.
    public static final String IMG_FILENAME = "asteroid.png";
    public static final int SPRITE_WIDTH = 128;
    public static final int SPRITE_HEIGHT = 128;
    public static final int NUM_SPRITES = 48;
    public static final int MILLIS_PER_FRAME = 40;

    // Constants for the entity size and physics.
    public static final int WIDTH = 70;
    public static final int HEIGHT = 70;
    public static final int COLLISION_RADIUS = WIDTH / 2;
    public static final double MASS = 100;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Asteroid(Kinematic2D kinematic, Grid grid, Animation animation) {
        super(kinematic, animation, grid, COLLISION_RADIUS, MASS);

        animation.start();
    }

    //--------------------------------------------------------------------------
    //  This class implements a simple update as it never receives steering or
    //  changes its velocity.
    //--------------------------------------------------------------------------
    @Override
    public void updateKinematic(double dt) {
        getKinematic().update(dt);
    }

    //--------------------------------------------------------------------------
    //  Overrides the 'kill' method to do nothing - because asteroids do not die
    //  and get removed. They just smash into the next thing.
    //--------------------------------------------------------------------------
    @Override
    public void kill() {}
}
