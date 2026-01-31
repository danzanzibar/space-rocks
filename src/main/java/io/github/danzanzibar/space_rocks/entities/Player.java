//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the player - a spaceship hell bent on killing UFOs.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.core.ResourceLoader;
import io.github.danzanzibar.game_framework.audio.Sound;
import io.github.danzanzibar.game_framework.graphics.Sprite;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksKeyBindings;
import io.github.danzanzibar.game_framework.audio.SoundManager;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;
import io.github.danzanzibar.steering.Steering2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Player extends CollisionEntity {

    // Constant Strings for media files.
    public static final String IMG_FILENAME_THRUST = "spaceship-thrust.png";
    public static final String IMG_FILENAME_NO_THRUST = "spaceship-no-thrust.png";
    public static final String SOUND_FILENAME = "thrusters.wav";

    // Constants defining player motion.
    public static final double ACCELERATION = 400.0;
    public static final double ROTATION = 3.5;
    public static final double MAX_SPEED = 800.0;

    // Constants defining player size and physics.
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;
    public static final int COLLISION_RADIUS = WIDTH / 2;
    public static final double MASS = 50;

    // Used to query the state of user inputs.
    private final KeyBindings<SpaceRocksKeyBindings> keyBindings;

    // Manages lasers. Since the player initiates all of these, it's natural to hold it here.
    private final LaserManager laserManager;

    // Loaded sound and image resources for the Player.
    private final BufferedImage thrust;
    private final BufferedImage noThrust;
    private final Sprite sprite;
    private final Sound thrusterSound;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Player(Kinematic2D kinematic, Grid grid, KeyBindings<SpaceRocksKeyBindings> keyBindings, SoundManager sounds) {
        super(kinematic, grid, COLLISION_RADIUS, MASS);

        this.keyBindings = keyBindings;

        // Store the two images and create the Sprite.
        thrust = ResourceLoader.loadImage(Player.IMG_FILENAME_THRUST);
        noThrust = ResourceLoader.loadImage(Player.IMG_FILENAME_NO_THRUST);
        sprite = new Sprite(noThrust, Player.WIDTH, Player.HEIGHT);
        setDrawable(sprite);

        // Store the thruster sound.
        thrusterSound = ResourceLoader.loadSound(SOUND_FILENAME);

        // Initialize the LaserManager.
        BufferedImage laserImg = ResourceLoader.loadImage(Laser.IMG_FILENAME);
        Sprite laserSprite = new Sprite(laserImg, Laser.WIDTH, Laser.HEIGHT);
        laserManager = new LaserManager(laserSprite, grid, sounds);
    }

    //--------------------------------------------------------------------------
    //  An override of the 'draw' method that calls the laser manager to draw
    //  lasers too.
    //--------------------------------------------------------------------------
    @Override
    public void draw(Graphics2D g2d, ImageObserver imageObserver) {
        super.draw(g2d, imageObserver);

        // Draw the lasers.
        laserManager.draw(g2d, imageObserver);
    }

    //--------------------------------------------------------------------------
    //  The player implementation of this method using steering to update the
    //  kinematic based on input.
    //--------------------------------------------------------------------------
    @Override
    public void updateKinematic(double dt) {

        // Update the laser manager first as we don't want to create a laser and move it in the same frame.
        laserManager.update(dt);

        if (isAlive()) {

            // Update the player kinematic.
            getKinematic().update(getSteering(), MAX_SPEED, dt);

            // Fire the laser if needed.
            if (keyBindings.getKeyState(SpaceRocksKeyBindings.FIRE))
                laserManager.fireLaser();

            // Update the sprite's image based on thrusters firing and play the sound if on.
            if (keyBindings.getKeyState(SpaceRocksKeyBindings.THRUST)) {
                sprite.setImg(thrust);
                if (!thrusterSound.isPlaying())
                    thrusterSound.play();
            } else {
                sprite.setImg(noThrust);
                if (thrusterSound.isPlaying())
                    thrusterSound.stop();
            }
        } else
            getKinematic().setVelocity(new Vector2(0, 0));   // If dead, stop moving.
    }

    //--------------------------------------------------------------------------
    //  This method creates the steering from inputs.
    //--------------------------------------------------------------------------
    private Steering2D getSteering() {
        Vector2 linear = new Vector2(0, 0);
        double angular = 0.0;

        // Get the user input states.
        boolean thrust = keyBindings.getKeyState(SpaceRocksKeyBindings.THRUST);
        boolean turnCW = keyBindings.getKeyState(SpaceRocksKeyBindings.CW);
        boolean turnCCW = keyBindings.getKeyState(SpaceRocksKeyBindings.CCW);

        // Get forward acceleration.
        if (thrust)
            linear = Vector2.fromAngle(getKinematic().getOrientation()).scale(ACCELERATION);

        // Get rotation.
        if (!turnCW && !turnCCW || turnCW && turnCCW)
            getKinematic().setRotation(0.0);

        else if (turnCW)
            getKinematic().setRotation(ROTATION);

        else
            getKinematic().setRotation(-ROTATION);

        return new Steering2D(linear, angular);
    }

    //--------------------------------------------------------------------------
    //  This method is overriden for the player as it never really dies, just
    //  dissapears.
    //--------------------------------------------------------------------------
    @Override
    public void kill() {
        super.kill();

        // The player doesn't get cleaned up when killed like other entities, so don't draw any longer.
        setDrawable(null);
    }

    //--------------------------------------------------------------------------
    //  A method to bring the player back when the game is restarted.
    //--------------------------------------------------------------------------
    public void resurrect() {
        alive = true;
        setDrawable(sprite);
    }
}
