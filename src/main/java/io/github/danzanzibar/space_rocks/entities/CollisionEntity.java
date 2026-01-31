//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This subclass of 'Entity' represents a collidable entity.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.graphics.Drawable;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

public abstract class CollisionEntity extends Entity {
    // The size of the colliding object. Represented simply as a circle.
    private final double radius;
    private final double mass;

    //--------------------------------------------------------------------------
    //  A constructor for the class that allows the 'Drawable' to be specified
    //  later.
    //--------------------------------------------------------------------------
    public CollisionEntity(Kinematic2D kinematic, Grid grid, double radius, double mass) {
        this(kinematic, null, grid, radius, mass);
    }

    //--------------------------------------------------------------------------
    //  A fully parametrized constructor for the class.
    //--------------------------------------------------------------------------
    public CollisionEntity(Kinematic2D kinematic, Drawable drawable, Grid grid, double radius, double mass) {
        super(kinematic, drawable, grid);

        this.radius = radius;
        this.mass = mass;
    }

    //--------------------------------------------------------------------------
    //  This returns a boolean indicating if the entity is colliding with
    //  another.
    //--------------------------------------------------------------------------
    public boolean isColliding(CollisionEntity other, Grid grid) {

        // Get the toroidal distance to check for a collision.
        double distance = grid.toroidalDistance(getKinematic().getPosition(), other.getKinematic().getPosition());
        double collisionRange = radius + other.radius;

        return distance <= collisionRange;
    }

    //--------------------------------------------------------------------------
    //  Returns the entity's mass.
    //--------------------------------------------------------------------------
    public double getMass() {
        return mass;
    }

    //--------------------------------------------------------------------------
    //  Returns the entity's collision radius.
    //--------------------------------------------------------------------------
    public double getRadius() {
        return radius;
    }
}
