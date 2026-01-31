//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a weighted steering behaviour.
//******************************************************************************
package io.github.danzanzibar.steering;

import io.github.danzanzibar.steering.behaviours.SteeringBehaviour;

public class WeightedBehaviour {
    private final SteeringBehaviour behaviour;
    private final double weight;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public WeightedBehaviour(SteeringBehaviour behaviour, double weight) {
        this.behaviour = behaviour;
        this.weight = weight;
    }

    //--------------------------------------------------------------------------
    //  Returns the 'Steering2D' object with fields scaled by 'weight'.
    //--------------------------------------------------------------------------
    public Steering2D getWeightedSteering() {
        Steering2D steering = behaviour.getSteering();

        return new Steering2D(steering.linear().scale(weight), steering.angular() * weight);
    }
}
