//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a blending of weighted steering behaviours.
//******************************************************************************
package io.github.danzanzibar.steering;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.steering.behaviours.SteeringBehaviour;

import java.util.ArrayList;
import java.util.List;

public class BlendedBehaviour {
    private final double maxAcceleration;
    private final double maxAngularAcceleration;
    private final List<WeightedBehaviour> behaviours;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public BlendedBehaviour(double maxAcceleration, double maxAngularAcceleration) {
        this.maxAcceleration = maxAcceleration;
        this.maxAngularAcceleration = maxAngularAcceleration;
        this.behaviours = new ArrayList<>();
    }

    //--------------------------------------------------------------------------
    //  Adds a new behaviour with a given weight.
    //--------------------------------------------------------------------------
    public void addBehaviour(SteeringBehaviour behaviour, double weight) {
        behaviours.add(new WeightedBehaviour(behaviour, weight));
    }

    //--------------------------------------------------------------------------
    //  Returns the combined steering of the weighted behaviours.
    //--------------------------------------------------------------------------
    public Steering2D getSteering() {
        Vector2 linear = new Vector2();
        double angular = 0.0;

        // Get each behaviour's contribution to the total steering.
        for (WeightedBehaviour behaviour : behaviours) {
            Steering2D steering = behaviour.getWeightedSteering();
            linear.translate(steering.linear());
            angular += steering.angular();
        }

        // Clamp to the maxAcceleration.
        if (linear.magnitude() > maxAcceleration)
            linear.setMagnitude(maxAcceleration);

        // Clamp to the maxAngularAcceleration.
        if (Math.abs(angular) > maxAngularAcceleration)
            angular = Math.copySign(maxAngularAcceleration, angular);

        return new Steering2D(linear, angular);
    }
}
