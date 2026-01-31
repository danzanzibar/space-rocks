//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a physics object that responds to accelerations, both
//  linear and angular. 
//******************************************************************************
package io.github.danzanzibar.steering;

import io.github.danzanzibar.game_framework.core.Vector2;

public class Kinematic2D {
    private Vector2 position;
    private Vector2 velocity;
    private double orientation;
    private double rotation;

    //--------------------------------------------------------------------------
    //  A default constructor for the class with zero-valued fields.
    //--------------------------------------------------------------------------
    public Kinematic2D() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.orientation = 0;
        this.rotation = 0;
    }

    //--------------------------------------------------------------------------
    //  A parametrized constructor for the class.
    //--------------------------------------------------------------------------
    public Kinematic2D(Vector2 position, Vector2 velocity, double orientation, double rotation) {
        this.position = position;
        this.velocity = velocity;
        this.orientation = orientation;
        this.rotation = rotation;
    }
    
    //--------------------------------------------------------------------------
    //  Getters and setters for the fields.
    //--------------------------------------------------------------------------
    
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    //--------------------------------------------------------------------------
    //  This method takes a 'Steering2D' object that holds accelerations, a
    //  maximum speed to clamp to, and an elapsed time. It updates the kinematic
    //  values of the objects using these parameters.
    //--------------------------------------------------------------------------
    public void update(Steering2D steering, double maxSpeed, double deltaTime) {

        // Translate the position and orientation by current rates.
        position.translate(velocity.scale(deltaTime));
        orientation += rotation * deltaTime;

        // Update the rates.
        velocity.translate(steering.linear().scale(deltaTime));
        rotation += steering.angular() * deltaTime;

        // Clamp velocity to maxSpeed.
        if (velocity.magnitude() > maxSpeed) {
            velocity.normalize();
            velocity = velocity.scale(maxSpeed);
        }
    }

    //--------------------------------------------------------------------------
    //  An alternate overload that assumes no accelerations are present and
    //  simply updates position based on the current velocity.
    //--------------------------------------------------------------------------
    public void update(double deltaTime) {

        // Just translate the position and orientation as no steering is supplied.
        position.translate(velocity.scale(deltaTime));
        orientation += rotation * deltaTime;
    }
}
