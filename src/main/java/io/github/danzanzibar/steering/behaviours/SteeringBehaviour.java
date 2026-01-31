//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This abstract class represents a steering behaviour. 
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

public abstract class SteeringBehaviour {
    protected Kinematic2D character;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public SteeringBehaviour(Kinematic2D character) {
        this.character = character;
    }

    public abstract Steering2D getSteering();
}
