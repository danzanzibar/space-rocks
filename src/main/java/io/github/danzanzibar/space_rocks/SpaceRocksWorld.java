//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This subclass of 'World' represents the game world of Space Rocks. It
//  contains all state of the actual game objects.
//******************************************************************************
package io.github.danzanzibar.space_rocks;

import io.github.danzanzibar.game_framework.audio.SoundManager;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.core.World;
import io.github.danzanzibar.game_framework.graphics.Drawable;
import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.entities.*;
import io.github.danzanzibar.space_rocks.spatial.Background;
import io.github.danzanzibar.space_rocks.entities.CollisionManager;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpaceRocksWorld extends World {
    public static final int VISIBLE_WIDTH = 1200;
    public static final int VISIBLE_HEIGHT = 1200;

    public static final Vector2 INITIAL_PLY_POS = new Vector2(3000, 3000);
    public static final int SCORE_PER_UFO = 100;

    // The core game objects.
    private final Background background;
    private final Grid grid;
    private final Player player;
    private final AsteroidManager asteroidManager;
    private final UFOManager ufoManager;
    private final CollisionManager collisionManager;

    private int score;

    // Used to send a view of the game world to the renderer.
    private final List<Drawable> drawables = new ArrayList<>();

    //--------------------------------------------------------------------------
    //  A constructor for the class that sets up all the enclosed game objects.
    //--------------------------------------------------------------------------
    public SpaceRocksWorld(KeyBindings<SpaceRocksKeyBindings> keyBindings) {

        // Create the background.
        background = new Background();

        // Use the background to generate the dimensions of user space and create the Grid.
        grid = new Grid(background.getUserSpaceDims());
        background.setGrid(grid);

        // Set up the sound manager.
        SoundManager soundManager = new SoundManager();
        soundManager.addSound(Laser.SOUND_FILENAME, 5);
        soundManager.addSound(Explosion.SOUND_FILENAME, 5);
        soundManager.addSound(UFO.ATTACK_SOUND_FILENAME, 5);
        soundManager.addSound(Player.SOUND_FILENAME, 5);

        // Create the player.
        Kinematic2D playerKinematic = new Kinematic2D(INITIAL_PLY_POS, new Vector2(), -Math.PI / 2.0, 0.0);
        player = new Player(playerKinematic, grid, keyBindings, soundManager);

        // Set up the entity managers.
        asteroidManager = new AsteroidManager(grid);
        ufoManager = new UFOManager(grid, soundManager);
        collisionManager = new CollisionManager(grid, soundManager);

        score = 0;
    }

    //--------------------------------------------------------------------------
    //  A getter for the current score.
    //--------------------------------------------------------------------------
    public int getScore() {
        return score;
    }

    //--------------------------------------------------------------------------
    //  A getter for the player's 'alive' flag.
    //--------------------------------------------------------------------------
    public boolean isPlayerAlive() {
        return player.isAlive();
    }

    //--------------------------------------------------------------------------
    //  Starts the demo at the launch of the game.
    //--------------------------------------------------------------------------
    public void startDemo() {
        grid.updateCell(player);

        // Remove the player for the demo.
        player.kill();

        // Make a lot of asteroids and ufos fly around for the demo.
        asteroidManager.setTargetNum(AsteroidManager.DEMO_NUM);
        asteroidManager.init();
        ufoManager.setTargetNum(UFOManager.DEMO_NUM);
        ufoManager.init();
    }

    //--------------------------------------------------------------------------
    //  Starts the actual game where the user takes control.
    //--------------------------------------------------------------------------
    public void startGame() {

        // Bring the player back.
        player.resurrect();

        // Reset the asteroids and UFOs.
        asteroidManager.clearEntities();
        asteroidManager.setTargetNum(AsteroidManager.INITIAL_NUM);
        asteroidManager.init();

        ufoManager.clearEntities();
        ufoManager.setTargetNum(UFOManager.INITIAL_NUM);
        ufoManager.init();

        // Reset the score.
        score = 0;
    }

    //--------------------------------------------------------------------------
    //  The implementation of the template method. Delegates updating to the
    //  various game objects before checking for collisions and updating the
    //  score.
    //--------------------------------------------------------------------------
    @Override
    protected void doUpdate(double dt) {

        // First update everyone's positions.
        player.update(dt);
        asteroidManager.update(dt);
        ufoManager.update(dt);
        collisionManager.update(dt);

        // Check for collisions and get the points for UFO collisions.
        score += collisionManager.checkCollisions();
    }

    //--------------------------------------------------------------------------
    //  Returns a "view" of the game - a list of Drawables.
    //--------------------------------------------------------------------------
    public List<Drawable> getView() {

        // First clear the list and add the background to be drawn first.
        drawables.clear();
        drawables.add(background);

        // Add asteroids.
        for (Entity asteroid : asteroidManager.getEntities())
            if (asteroid.getCell().isVisible())
                drawables.add(asteroid);

        // Add UFOs.
        for (Entity ufo : ufoManager.getEntities())
            if (ufo.getCell().isVisible())
                drawables.add(ufo);

        // Add explosions.
        for (Entity explosion : collisionManager.getEntities())
            if (explosion.getCell().isVisible())
                drawables.add(explosion);

        // Finally draw the player last.
        drawables.add(player);

        return drawables;
    }

    //--------------------------------------------------------------------------
    //  The user space transform that centered the game on the player.
    //--------------------------------------------------------------------------
    public void userSpaceTransform(Graphics2D g2d, Dimension frameSize) {

        // Translate it so the player sits in the middle of the screen.
        g2d.translate(frameSize.width / 2, frameSize.height / 2);

        // Scale the game to fit. Use the intended scale for the game, not the actual frame size so we don't change
        // the perspective if the frame was resized to a non-square shape.
        double xScale = (double) SpaceRocksApp.PANEL_SIZE.width / VISIBLE_WIDTH;
        double yScale = (double) SpaceRocksApp.PANEL_SIZE.height / VISIBLE_HEIGHT;
        g2d.scale(xScale, yScale);

        Vector2 playerPos = player.getKinematic().getPosition();
        g2d.translate(-playerPos.getX(), -playerPos.getY());
    }
}
