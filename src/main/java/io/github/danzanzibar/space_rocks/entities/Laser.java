//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a laser bolt.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.graphics.Sprite;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

public class Laser extends CollisionEntity {

    // Constants for the media files.
    public static final String IMG_FILENAME = "laser.png";
    public static final String SOUND_FILENAME = "laser.wav";

    // Constants for the physics of the entity.
    public static final double SPEED = 1000.0;
    public static final double RANGE = 300.0;
    public static final double LIFE_TIME = RANGE / SPEED;
    public static final double MASS = 10;

    // Constants for the entity size.
    public static final int WIDTH = 15;
    public static final int HEIGHT = 2 * WIDTH;
    public static final double COLLISION_RADIUS = WIDTH;

    // An accumulator for the laser to monitor its lifetime.
    private double acc;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Laser(Kinematic2D kinematic, Grid grid, Sprite sprite) {
        super(kinematic, sprite, grid, COLLISION_RADIUS, MASS);

        acc = 0;
    }

    //--------------------------------------------------------------------------
    //  The implementation of the abstract method. Kills the laser if past its
    //  lifetime.
    //--------------------------------------------------------------------------
    @Override
    public void updateKinematic(double dt) {
        acc += dt;

        // If past its lifetime, kill it.
        if (acc > LIFE_TIME)
            kill();
        else
            getKinematic().update(dt);
    }
}
