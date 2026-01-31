//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  A manager class for lasers.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.audio.SoundManager;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.graphics.Sprite;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

public class LaserManager extends EntityManager {
    public static final int ORIGIN_OFFSET = 30 + Laser.HEIGHT / 2;
    public static final long COOLDOWN = (long) (0.3 * 1_000_000_000);

    // Monitors last time firing.
    private long timeSinceFiring;
    private final Sprite sprite;
    private final SoundManager sounds;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public LaserManager(Sprite sprite, Grid grid, SoundManager sounds) {
        super(grid);

        timeSinceFiring = 0;
        this.sprite = sprite;
        this.sounds = sounds;
    }

    //--------------------------------------------------------------------------
    //  Adds a laser.
    //--------------------------------------------------------------------------
    public void fireLaser() {

        // Check that the laser isn't on cooldown.
        long now = System.nanoTime();
        if (now - timeSinceFiring > COOLDOWN) {
            Kinematic2D playerKinematic = getGrid().getPlayer().getKinematic();

            Vector2 playerPos = playerKinematic.getPosition();
            double playerOrientation = playerKinematic.getOrientation();
            Vector2 playerVelocity = playerKinematic.getVelocity();

            // Get the offset along the direction the player is oriented for an intial laser position.
            Vector2 posOffset = Vector2.fromAngle(playerOrientation).scale(ORIGIN_OFFSET);
            Vector2 pos = playerPos.add(posOffset);
            getGrid().gridMod(pos);   // Make sure this stays on the grid.

            // Add the base laser velocity to the current player's.
            Vector2 velocityOffset = Vector2.fromAngle(playerOrientation).scale(Laser.SPEED);
            Vector2 velocity = playerVelocity.add(velocityOffset);

            Kinematic2D kinematic = new Kinematic2D(pos, velocity, playerOrientation, 0.0);

            Laser laser = new Laser(kinematic, getGrid(), sprite);
            addEntity(laser);

            // Play the awesome sound and reset the cooldown.
            sounds.playSound(Laser.SOUND_FILENAME);
            timeSinceFiring = now;
        }
    }
}
