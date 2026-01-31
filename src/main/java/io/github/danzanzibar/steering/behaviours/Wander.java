//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the "Wander" steering behaviour.
//******************************************************************************
package io.github.danzanzibar.steering.behaviours;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

import static io.github.danzanzibar.steering.SteeringUtils.randomBinomial;

public class Wander extends SteeringBehaviour {
    private final double wanderOffset;
    private final double wanderRadius;
    private final double wanderRate;
    private final double maxAcceleration;

    private double wanderOrientation;
    private final Face face;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Wander(Kinematic2D character, double wanderOffset, double wanderRadius, double wanderRate,
                  double maxAcceleration, double maxAngularAcceleration, double maxRotation, double targetRadius,
                  double slowRadius, double timeToTarget) {
        super(character);

        this.wanderOffset = wanderOffset;
        this.wanderRadius = wanderRadius;
        this.wanderRate = wanderRate;
        this.maxAcceleration = maxAcceleration;

	// Create the face object with a blank target.
        face = new Face(character, maxAngularAcceleration, maxRotation, targetRadius, slowRadius, timeToTarget,
                new Kinematic2D());

        // Start off with a random wander orientation.
        wanderOrientation = Math.random() * Math.PI * 2;
    }

    //--------------------------------------------------------------------------
    //  This method returns the 'Steering2D' object output of the algorithm.
    //--------------------------------------------------------------------------
    @Override
    public Steering2D getSteering() {

        // Update the wander orientation.
        wanderOrientation += randomBinomial() * wanderRate;

        // Get the orientation of the target relative to the coordinate system.
        double characterOrientation = character.getOrientation();
        double targetOrientation = wanderOrientation + characterOrientation;

        // Get the vector from the character to the wander circle. Then get the wander circle's centre.
        Vector2 wanderOffsetVector = Vector2.fromAngle(characterOrientation).scale(wanderOffset);
        Vector2 wanderCircleCentre = character.getPosition().add(wanderOffsetVector);

        // Get the target position using the wander radius and target orientation.
        Vector2 radialVectorToTarget = Vector2.fromAngle(targetOrientation).scale(wanderRadius);
        Vector2 targetPos = wanderCircleCentre.add(radialVectorToTarget);

        // Get the Face-based angular acceleration for this position, then add full forward acceleration.
        face.target.setPosition(targetPos);
        Steering2D steering = face.getSteering();
        Vector2 forwardAcceleration = Vector2.fromAngle(characterOrientation).scale(maxAcceleration);

        return new Steering2D(forwardAcceleration, steering.angular());
    }
}
