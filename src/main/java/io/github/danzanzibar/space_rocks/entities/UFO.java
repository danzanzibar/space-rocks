//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a scary, suicidal UFO.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.graphics.Animation;
import io.github.danzanzibar.game_framework.audio.SoundManager;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.*;
import io.github.danzanzibar.steering.behaviours.Wander;
import io.github.danzanzibar.steering.behaviours.ToroidalArrive;

public class UFO extends CollisionEntity {
    // Constants for media file names.
    public static final String ATTACK_SOUND_FILENAME = "attack.wav";
    public static final String IMG_FILENAME = "ufo.png";

    // Constants for the sprite sheet and animation.
    public static final int PIXEL_WIDTH = 80;
    public static final int PIXEL_HEIGHT = 80;
    public static final int NUM_SPRITES = 20;
    public static final int MILLIS_PER_FRAME = 40;

    // Constants for entity size and physics.
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static final int COLLISION_RADIUS = WIDTH / 2;
    public static final double MASS = 50;
    public static final double MAX_SPEED = 600.0;
    public static final double ACCELERATION = 800.0;
    public static final double ANGULAR_ACCELERATION = 3.0;
    public static final double MAX_ROTATION = 3.0;

    // Constants for the Wander behaviour.
    public static final double TARGET_RADIUS = 0.1;
    public static final double SLOW_RADIUS = 0.6;
    public static final double TIME_TO_TARGET = 0.1;
    public static final double WANDER_OFFSET = 5.5;
    public static final double WANDER_RADIUS = 1.0;
    public static final double WANDER_RATE = 0.1;

    // Constants for the Arrive behaviour.
    public static final double ATTACK_RANGE = 400;
    public static final double ARRIVE_TARGET_RADIUS = 100;
    public static final double ARRIVE_SLOW_RADIUS = 100;
    public static final double ARRIVE_TIME_TO_TARGET = 0.1;

    // Stores the current steering behaviour.
    private BlendedBehaviour steering;

    // Stores the various behaviours to switch between.
    private final BlendedBehaviour wanderSteering;
    private final BlendedBehaviour attackSteering;

    // Stores the collision avoidance system.
    private final UFOCollisionAvoidance collisionAvoidance;

    private final SoundManager sounds;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public UFO(Kinematic2D kinematic, Grid grid, Animation animation, SoundManager sounds) {
        super(kinematic, animation, grid, COLLISION_RADIUS, MASS);

        this.sounds = sounds;

        // Create the various steering behaviours.
        collisionAvoidance = new UFOCollisionAvoidance(this, grid);
        Wander wander = new Wander(getKinematic(), WANDER_OFFSET, WANDER_RADIUS, WANDER_RATE, ACCELERATION,
                ANGULAR_ACCELERATION, MAX_ROTATION, TARGET_RADIUS, SLOW_RADIUS, TIME_TO_TARGET);
        ToroidalArrive arrive = new ToroidalArrive(grid, getKinematic(), ACCELERATION, MAX_SPEED,
                ARRIVE_TARGET_RADIUS, ARRIVE_SLOW_RADIUS, ARRIVE_TIME_TO_TARGET, grid.getPlayer().getKinematic());


        // Create the two blended steering behaviours.
        wanderSteering = new BlendedBehaviour(ACCELERATION, ANGULAR_ACCELERATION);
        wanderSteering.addBehaviour(wander, 1.0);
        wanderSteering.addBehaviour(collisionAvoidance, 3.0);

        attackSteering = new BlendedBehaviour(ACCELERATION, ANGULAR_ACCELERATION);
        attackSteering.addBehaviour(arrive, 1.0);
        attackSteering.addBehaviour(collisionAvoidance, 1.0);

        // Set to wander.
        steering = wanderSteering;

        animation.start();
    }

    //--------------------------------------------------------------------------
    //  The UFO implementation of the method. It checks if the steering
    //  behaviour needs to change based on the player position.
    //--------------------------------------------------------------------------
    @Override
    public void updateKinematic(double dt) {
        Player player = grid.getPlayer();
        double distanceToPlayer = grid.toroidalDistance(player.getKinematic().getPosition(), getKinematic().getPosition());

        // If close to the player, go into attach mode (Arrive). Play a sound to scare the user.
        if (distanceToPlayer < ATTACK_RANGE && player.isAlive()) {
            if (steering == wanderSteering)
                sounds.playSound(ATTACK_SOUND_FILENAME);
            steering = attackSteering;
        }

        // If very far from the player, resume wandering.
        else if (distanceToPlayer > 3 * ATTACK_RANGE || !player.isAlive())
            steering = wanderSteering;

        // If wandering, avoid other UFOs. If trying to kill the character, well, I suppose they shouldn't care.
        boolean avoidUfos = steering == wanderSteering;
        collisionAvoidance.updateTargets(avoidUfos);

        Steering2D steeringOutput = steering.getSteering();

        // Use a lower max speed when wandering.
        double maxSpeed = (steering == wanderSteering) ? MAX_SPEED / 2.0 : MAX_SPEED;
        getKinematic().update(steeringOutput, maxSpeed, dt);
    }
}
