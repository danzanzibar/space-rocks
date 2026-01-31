//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the "Arrive" steering behaviour.
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

public class Arrive extends SingleTargetSteeringBehaviour {

    private final double maxAcceleration;
    private final double maxSpeed;
    private final double targetRadius;
    private final double slowRadius;
    private final double timeToTarget;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Arrive(Kinematic2D character, double maxAcceleration, double maxSpeed, double targetRadius,
                  double slowRadius, double timeToTarget, Kinematic2D target) {
        super(character, target);

        this.maxAcceleration = maxAcceleration;
        this.maxSpeed = maxSpeed;
        this.targetRadius = targetRadius;
        this.slowRadius = slowRadius;
        this.timeToTarget = timeToTarget;
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Steering2D' object output of the algorithm.
    //--------------------------------------------------------------------------
    @Override
    public Steering2D getSteering() {
        Vector2 targetPos = target.getPosition();

        // Get the direction and magnitude to the target.
        Vector2 direction = targetPos.subtract(character.getPosition());
        double distance = direction.magnitude();

        // If already there, return no (i.e. zero) steering.
        if (distance < targetRadius)
            return new Steering2D();

        // Get max acceleration if outside the slow radius, otherwise get a proportional value.
        double targetSpeed;
        if (distance > slowRadius)
            targetSpeed = maxSpeed;
        else
            targetSpeed = maxSpeed * distance / slowRadius;

        // Calculate the target velocity.
        direction.normalize();
        Vector2 targetVelocity = direction.scale(targetSpeed);

        // Calculate the acceleration needed to get to the target velocity.
        Vector2 acceleration = targetVelocity.subtract(character.getVelocity()).scale(1 / timeToTarget);

        // Clamp the acceleration to max.
        if (acceleration.magnitude() > maxAcceleration)
            acceleration.setMagnitude(maxAcceleration);

        return new Steering2D(acceleration, 0.0);
    }
}
