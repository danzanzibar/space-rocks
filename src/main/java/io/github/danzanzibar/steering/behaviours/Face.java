//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the "Face" steering behaviour.
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

public class Face extends SingleTargetSteeringBehaviour {
    private final Align align;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Face(Kinematic2D character, double maxAngularAcceleration, double maxRotation, double targetRadius,
                double slowRadius, double timeToTarget, Kinematic2D target) {
        super(character, target);

        align = new Align(character, maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget,
                new Kinematic2D());
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Steering2D' object output of the algorithm.
    //--------------------------------------------------------------------------
    @Override
    public Steering2D getSteering() {
        Vector2 targetPos = target.getPosition();

        // Get vector to target.
        Vector2 direction = targetPos.subtract(character.getPosition());

        // If zero length, return zero steering.
        if (direction.magnitude() == 0)
            return new Steering2D();

        // Get the angle.
        double targetOrientation = direction.toAngle();

        align.target.setOrientation(targetOrientation);

        return align.getSteering();
    }
}
