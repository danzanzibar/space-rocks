//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This abstract class represents a steering behaviour that utilizes a single
//  target in its algorithm. 
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

public abstract class SingleTargetSteeringBehaviour extends SteeringBehaviour {
    protected Kinematic2D target;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public SingleTargetSteeringBehaviour(Kinematic2D character, Kinematic2D target) {
        super(character);

        this.target = target;
    }
}
