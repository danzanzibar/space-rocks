//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class manages the asteroids in game.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.core.ResourceLoader;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.graphics.Animation;
import io.github.danzanzibar.game_framework.graphics.SpriteSheet;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

import java.awt.image.BufferedImage;

public class AsteroidManager extends EntityManager {
    public static final int INITIAL_NUM = 40;
    public static final int DEMO_NUM = 80;
    public static final double MAX_SPEED = 200;
    public static final double MAX_ROTATION = 2;

    private SpriteSheet spriteSheet;
    private int targetNum;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public AsteroidManager(Grid grid) {
        super(grid);

        // Load and cache the Spritesheet for creating new Asteroids.
        BufferedImage img = ResourceLoader.loadImage(Asteroid.IMG_FILENAME);
        spriteSheet = new SpriteSheet(img, Asteroid.SPRITE_WIDTH, Asteroid.SPRITE_HEIGHT, Asteroid.NUM_SPRITES);

        targetNum = INITIAL_NUM;
    }

    //--------------------------------------------------------------------------
    //  A setter for 'targetNum'.
    //--------------------------------------------------------------------------
    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum;
    }

    //--------------------------------------------------------------------------
    //  This method sets up the asteroids in the game.
    //--------------------------------------------------------------------------
    public void init() {
        addAsteroids(targetNum);
    }

    //--------------------------------------------------------------------------
    //  This method adds 'n' asteroids to the game.
    //--------------------------------------------------------------------------
    public void addAsteroids(int n) {
        for (int i = 0; i < n; i++)
            addAsteroid();
    }

    //--------------------------------------------------------------------------
    //  This method adds an asteroid to the game in non-visible space.
    //--------------------------------------------------------------------------
    private void addAsteroid() {

        // Get a safe (non-visible) spawn location for the new asteroid.
        Vector2 pos = getGrid().getSafeSpawnPos();

        // Get a random speed, up to MAX_SPEED.
        double speed = Math.random() * MAX_SPEED;

        // Get a random direction.
        double velocityDirection = Math.random() * Math.PI * 2;
        Vector2 velocity = Vector2.fromAngle(velocityDirection).scale(speed);

        // Get a random orientation.
        double orientation = Math.random() * Math.PI * 2;

        // Get a random rotation, up to MAX_ROTATION.
        double rotation = Math.random() * MAX_ROTATION;
        Kinematic2D kinematic = new Kinematic2D(pos, velocity, orientation, rotation);

        // Create a new animation with a random initial frame so the initial asteroids don't all look the same.
        Animation animation = new Animation(spriteSheet, Asteroid.WIDTH, Asteroid.HEIGHT, Asteroid.MILLIS_PER_FRAME);
        int startingFrame = (int) (Math.random() * animation.getNumSprites());
        animation.setImgIndex(startingFrame);

        Asteroid asteroid = new Asteroid(kinematic, getGrid(), animation);
        addEntity(asteroid);
    }
}
