//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the "Align" steering behaviour.
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;
import io.github.danzanzibar.steering.SteeringUtils;

public class Align extends SingleTargetSteeringBehaviour {
    private final double maxAngularAcceleration;
    private final double maxRotation;
    private final double targetRadius;
    private final double slowRadius;
    private final double timeToTarget;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Align(Kinematic2D character, double maxAngularAcceleration, double maxRotation, double targetRadius,
                 double slowRadius, double timeToTarget, Kinematic2D target) {
        super(character, target);

        this.maxAngularAcceleration = maxAngularAcceleration;
        this.maxRotation = maxRotation;
        this.targetRadius = targetRadius;
        this.slowRadius = slowRadius;
        this.timeToTarget = timeToTarget;
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Steering2D' object output of the algorithm.
    //--------------------------------------------------------------------------
    @Override
    public Steering2D getSteering() {
        double targetOrientation = target.getOrientation();

        // Get the necessary turn mapped to (-PI, PI].
        double rotation = SteeringUtils.mapToPlusMinusPi(targetOrientation - character.getOrientation());
        double rotMagnitude = Math.abs(rotation);

        // If we are already there, return zero steering.
        if (rotMagnitude < targetRadius)
            return new Steering2D();

        // If outside the slow radius, use maximal acceleration. Otherwise, scale the rotation.
        double targetRotation;
        if (rotMagnitude > slowRadius)
            targetRotation = maxRotation;
        else
            targetRotation = maxRotation * rotMagnitude / slowRadius;

        // Get the correct sign (angular direction) for the target rotation.
        if (rotation < 0)
            targetRotation = -targetRotation;
        
        // Calculate the angular acceleration needed to achieve target rotation.
        double angularAcceleration = (targetRotation - character.getRotation()) / timeToTarget;
        
        // Clamp the angular acceleration to max if over. Make sure it maintains the correct sign.
        if (Math.abs(angularAcceleration) > maxAngularAcceleration)
            angularAcceleration = (angularAcceleration < 0) ? -maxAngularAcceleration : maxAngularAcceleration;
        
        return new Steering2D(new Vector2(), angularAcceleration);
    }
}
