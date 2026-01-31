//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents an "Arrive" steering behaviour that handles a toroidal
//  user space properly. It must be supplied a 'Grid' object representing the
//  user space.
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

public class ToroidalArrive extends SingleTargetSteeringBehaviour {
    private final Grid grid;
    private final Arrive arrive;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public ToroidalArrive(Grid grid, Kinematic2D character, double maxAcceleration, double maxSpeed,
                          double targetRadius, double slowRadius, double timeToTarget, Kinematic2D target) {
        super(character, target);

        this.grid = grid;
        arrive = new Arrive(character, maxAcceleration, maxSpeed, targetRadius, slowRadius, timeToTarget,
                new Kinematic2D());
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Steering2D' object output of the algorithm.
    //--------------------------------------------------------------------------
    @Override
    public Steering2D getSteering() {
        Vector2 targetPos = target.getPosition();

        // Change the target position to be nearer the character if wrapping is occurring. Then delegate to the
        // 'Arrive' behaviour.
        Vector2 characterPos = arrive.character.getPosition();
        Vector2 signedDelta = grid.signedDelta(targetPos, characterPos);
        Vector2 toroidalTargetPos = characterPos.add(signedDelta);

        arrive.target.setPosition(toroidalTargetPos);

        return arrive.getSteering();
    }
}
