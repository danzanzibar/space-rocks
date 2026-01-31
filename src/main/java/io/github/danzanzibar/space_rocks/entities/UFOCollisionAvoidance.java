//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class is the collision avoidance steering behaviour for the UFOs.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.space_rocks.spatial.Cell;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Steering2D;
import io.github.danzanzibar.steering.behaviours.SteeringBehaviour;

import java.util.ArrayList;
import java.util.List;

public class UFOCollisionAvoidance extends SteeringBehaviour {
    public static final double RANGE = 300.0;

    private final UFO ufo;
    private final Grid grid;
    private final List<CollisionEntity> targets = new ArrayList<>();

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public UFOCollisionAvoidance(UFO ufo, Grid grid) {
        super(ufo.getKinematic());

        this.ufo = ufo;
        this.grid = grid;
    }

    //--------------------------------------------------------------------------
    //  Adds targets to be avoided.
    //--------------------------------------------------------------------------
    public void addTargets(List<? extends CollisionEntity> targets) {
        this.targets.addAll(targets);
    }

    //--------------------------------------------------------------------------
    //  Clears the target list.
    //--------------------------------------------------------------------------
    public void clearTargets() {
        this.targets.clear();
    }

    //--------------------------------------------------------------------------
    //  This method recreates the list of targets based on a range from the
    //  enclosed UFO.
    //--------------------------------------------------------------------------
    public void updateTargets(boolean includeUfos) {

        // First clear the previous targets, then get the cells that are in RANGE.
        clearTargets();
        Cell[] cellsToCheck = grid.getCellNeighbourhood(ufo.getCell(), RANGE);

        // Add asteroids (and UFOs if avoiding them).
        for (Cell cell : cellsToCheck) {
            addTargets(cell.getAsteroids());

            if (includeUfos)
                addTargets(cell.getUfos());
        }
    }

    //--------------------------------------------------------------------------
    //  This method calculates the steering output of the behaviour.
    //--------------------------------------------------------------------------
    @Override
    public Steering2D getSteering() {
        // Start with setting the shortest time to something greater than any collision time might be.
        double shortestTime = 100.0;

        // Cache a couple of ufo values.
        Vector2 ufoPos = ufo.getKinematic().getPosition();
        Vector2 ufoVel = ufo.getKinematic().getVelocity();
        double ufoRadius = ufo.getRadius();

        // Set the found target to null for now.
        CollisionEntity firstTarget = null;

        // Other variables to hold target data. I have to initialize these or the compiler complains even though
        // they can't logically be accessed.
        double firstMinSeparation = 0.0;
        double firstDistance = 0.0;
        double firstRadius = 0.0;
        Vector2 firstRelativePos = new Vector2();
        Vector2 firstRelativeVel = new Vector2();

        // Loop through available targets.
        for (CollisionEntity target : targets) {

            // We might be passing the ufo to as a target here, so pass on that.
            if (target == ufo)
                continue;

            // First we get the relative displacement and velocity to calculate a time to minimal separation.

            // To get the relative position, we need to use toroidal aware math.
            Vector2 relativePos = grid.signedDelta(target.getKinematic().getPosition(), ufoPos);
            double distance = relativePos.magnitude();
            Vector2 relativeVel = target.getKinematic().getVelocity().subtract(ufoVel);
            double relativeSpeed = relativeVel.magnitude();
            double timeToCollision = -relativePos.dot(relativeVel) / (relativeSpeed * relativeSpeed);

            // We underestimate the minimal separation value by calculating it as through the entities were
            // moving directly at each other. Hence, the minimal separation may even be negative. But I suppose
            // this may be faster because we avoid any vector math.
            double minSeparation = distance - relativeSpeed * timeToCollision;

            // If there could be no possibility of a collision, disregard this target.
            if (minSeparation > ufoRadius + target.getRadius())
                continue;

            // If we have a positive time and it's shortest so far, save all the info.
            if (timeToCollision > 0 && timeToCollision < shortestTime) {
                shortestTime = timeToCollision;
                firstTarget = target;
                firstMinSeparation = minSeparation;
                firstDistance = distance;
                firstRadius = target.getRadius();
                firstRelativePos = relativePos;
                firstRelativeVel = relativeVel;
            }
        }

        // If there's no target, return zero steering.
        if (firstTarget == null)
            return new Steering2D();

        // If we hit exactly or are already in collision, use the current position, otherwise calculate it.
        Vector2 relativePos;
        if (firstMinSeparation <= 0 || firstDistance < ufoRadius + firstRadius)
            relativePos = grid.signedDelta(firstTarget.getKinematic().getPosition(), ufoPos);
        else
            relativePos = firstRelativePos.add(firstRelativeVel.scale(shortestTime));

        // Calculate the avoidance acceleration.
        relativePos.setMagnitude(UFO.ACCELERATION);
        Vector2 linear = relativePos.scale(-1.0);

        return new Steering2D(linear, 0.0);
    }
}
