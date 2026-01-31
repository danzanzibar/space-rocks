//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class serves two purposes: (1) it acts as a manager class for
//  explosions and (2) does all collision checking. Since collisions are what
//  generate explosions, it seems reasonable to allow this dual purpose.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.audio.SoundManager;
import io.github.danzanzibar.game_framework.core.ResourceLoader;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.graphics.OneShotAnimation;
import io.github.danzanzibar.game_framework.graphics.SpriteSheet;
import io.github.danzanzibar.space_rocks.SpaceRocksWorld;
import io.github.danzanzibar.space_rocks.spatial.Cell;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

import java.awt.image.BufferedImage;
import java.util.List;

public class CollisionManager extends EntityManager {
    private final SpriteSheet spriteSheet;
    private final SoundManager sounds;

    private int scoreEarned;

    public CollisionManager(Grid grid, SoundManager sounds) {
        super(grid);

        // Create and cache the spritesheet for new Explosions.
        BufferedImage explosionImg = ResourceLoader.loadImage(Explosion.IMG_FILENAME);
        spriteSheet = new SpriteSheet(explosionImg, Explosion.SPRITE_WIDTH,
                Explosion.SPRITE_HEIGHT, Explosion.NUM_SPRITES);

        this.sounds = sounds;

        scoreEarned = 0;
    }

    //--------------------------------------------------------------------------
    //  This is the primary collision checking method. Will check for any
    //  collisions in visible space and return the score earned due to any UFOs
    //  destroyed.
    //--------------------------------------------------------------------------
    public int checkCollisions() {

        // Reset the score count.
        scoreEarned = 0;

        // We're only going to bother checking visible space for collisions.
        Cell[][] visibleCells = getGrid().getVisibleCells();
        int numRows = visibleCells.length;
        int numCols = visibleCells[0].length;

        // Go through all visible cells to check for collisions.
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Cell cell = visibleCells[row][col];

                // First we check intra-cell collisions.
                checkIntraCellCollisions(cell);

                // Now we only check surrounding cells to the right, below, below and right, and below and left.
                // This avoids duplicate pairwise checking. These cells may not exist in visible space, so we check
                // them if contained in the array.
                boolean right = col + 1 < numCols;
                boolean below = row + 1 < numRows;
                boolean belowRight = right && below;
                boolean belowLeft = below && col - 1 >= 0;
                if (right)
                    checkInterCellCollisions(cell, visibleCells[row][col + 1]);
                if (below)
                    checkInterCellCollisions(cell, visibleCells[row + 1][col]);
                if (belowRight)
                    checkInterCellCollisions(cell, visibleCells[row + 1][col + 1]);
                if (belowLeft)
                    checkInterCellCollisions(cell, visibleCells[row + 1][col - 1]);
            }
        }

        return scoreEarned;
    }

    //--------------------------------------------------------------------------
    //  This private method checks for collisions between two adjacent cells.
    //--------------------------------------------------------------------------
    private void checkInterCellCollisions(Cell cell, Cell otherCell) {

        // Get the player entity from each cell. At least one will be null.
        Player player = cell.getPlayer();
        Player otherPlayer = otherCell.getPlayer();

        // Get the rest of the entities from the cells, but we will keep them organized by type and which cell they are
        // from.
        List<Asteroid> asteroids = cell.getAsteroids();
        List<UFO> ufos = cell.getUfos();
        List<Laser> lasers = cell.getLasers();

        // Get the entities from the other cell.
        List<Asteroid> otherAsteroids = otherCell.getAsteroids();
        List<UFO> otherUfos = otherCell.getUfos();
        List<Laser> otherLasers = otherCell.getLasers();

        // Check if the player (if present) collides with any asteroids or UFOs of the adjacent cell.
        if (player != null) {
            checkCollisions(player, otherAsteroids);
            checkCollisions(player, otherUfos);
        } else if (otherPlayer != null) {
            checkCollisions(otherPlayer, asteroids);
            checkCollisions(otherPlayer, ufos);
        }

        // Note we only check one cells entities (of a given type) against appropriate types from the other cell.
        // All intra-cell checks are made previously in another method.

        // Check lasers and asteroids.
        checkCollisions(lasers, otherAsteroids);
        checkCollisions(otherLasers, asteroids);

        // Check lasers and UFOs.
        checkCollisions(lasers, otherUfos);
        checkCollisions(otherLasers, ufos);

        // Check asteroids and UFOs.
        checkCollisions(asteroids, otherUfos);
        checkCollisions(otherAsteroids, ufos);

        // Finally, check UFOs against UFOs.
        checkCollisions(ufos, otherUfos);
    }

    //--------------------------------------------------------------------------
    //  This private method checks for collisions within a given cell.
    //--------------------------------------------------------------------------
    private void checkIntraCellCollisions(Cell cell) {
        Player player = cell.getPlayer();
        List<Asteroid> asteroids = cell.getAsteroids();
        List<UFO> ufos = cell.getUfos();
        List<Laser> lasers = cell.getLasers();

        // Check the player (if present) against asteroids and UFOs.
        if (player != null) {
            checkCollisions(player, asteroids);
            checkCollisions(player, ufos);
        }

        // Check the other relevant interactions.
        checkCollisions(lasers, asteroids);
        checkCollisions(lasers, ufos);
        checkCollisions(asteroids, ufos);
        checkCollisions(ufos);   // This overload checks the UFOs against the UFOs.
    }

    //--------------------------------------------------------------------------
    //  This overload checks for collisions within a list of entities. It
    //  disregards checks between one entity and itself.
    //--------------------------------------------------------------------------
    private void checkCollisions(List<? extends CollisionEntity> entities) {

        // We're checking a list against itself here.
        for (int i = 0; i < entities.size(); i++) {
            CollisionEntity entity = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                checkCollision(entity, entities.get(j));
            }
        }
    }

    //--------------------------------------------------------------------------
    //  This overload checks for collisions between two separate lists of
    //  entities.
    //--------------------------------------------------------------------------
    private void checkCollisions(List<? extends CollisionEntity> entities,
                                List<? extends CollisionEntity> otherEntities) {
        for (CollisionEntity entity : entities)
            checkCollisions(entity, otherEntities);
    }

    //--------------------------------------------------------------------------
    //  This overload checks for collision between one entity and a list of
    //  entities.
    //--------------------------------------------------------------------------
    private void checkCollisions(CollisionEntity entity, List<? extends CollisionEntity> otherEntities) {
        for (CollisionEntity otherEntity : otherEntities)
            checkCollision(entity, otherEntity);
    }

    //--------------------------------------------------------------------------
    //  This method checks for - and handles - a collision between two entities.
    //  It will create an explosion is found.
    //--------------------------------------------------------------------------
    private void checkCollision(CollisionEntity entity1, CollisionEntity entity2) {

        // The conditional checking is a little ugly here, but we need (1) both entities to be alive, (2) the entities
        // not to be the same entity, and (3) the two entities to be colliding.
        boolean bothAlive = entity1.isAlive() && entity2.isAlive();
        if (bothAlive && (entity1 != entity2) && entity1.isColliding(entity2, getGrid())) {

            // Get the midpoint of the entities for the explosion position. We need to use signed delta to handle
            // wrapping.
            Vector2 pos1 = entity1.getKinematic().getPosition();
            Vector2 pos2 = entity2.getKinematic().getPosition();
            Vector2 pos = pos1.add(getGrid().signedDelta(pos2, pos1).scale(0.5));
            getGrid().gridMod(pos);   // Make sure this stays on the grid.

            // Calculate the velocity of the explosion using conservation of momentum.
            double mass1 = entity1.getMass();
            double mass2 = entity2.getMass();
            Vector2 velocity1 = entity1.getKinematic().getVelocity().scale(mass1);
            Vector2 velocity2 = entity2.getKinematic().getVelocity().scale(mass2);
            Vector2 velocity = velocity1.add(velocity2).scale(1.0 / (mass1 + mass2));

            int explosionSize = 4;

            // Another ugly condition check, but we want explosions between asteroids and lasers to be smaller.
            if ((entity1 instanceof Laser && entity2 instanceof Asteroid) ||
                    (entity2 instanceof Laser && entity1 instanceof Asteroid))
                explosionSize = 1;

            // Add the explosion and kill the entities.
            addExplosion(pos, velocity, explosionSize);
            entity1.kill();
            entity2.kill();

            // Add to the score earned for each UFO if the player wasn't involved.
            if (!(entity1 instanceof Player)) {
                if (entity1 instanceof UFO)
                    scoreEarned += SpaceRocksWorld.SCORE_PER_UFO;
                if (entity2 instanceof UFO)
                    scoreEarned += SpaceRocksWorld.SCORE_PER_UFO;
            }
        }
    }

    //--------------------------------------------------------------------------
    //  This method adds an explosion.
    //--------------------------------------------------------------------------
    private void addExplosion(Vector2 pos, Vector2 velocity, int explosionSize) {

        // Get a random orientation for the explosion.
        double orientation = Math.random() * Math.PI * 2;
        Kinematic2D kinematic = new Kinematic2D(pos, velocity, orientation, 0.0);

        // Create the animation for the explosion.
        OneShotAnimation animation = new OneShotAnimation(spriteSheet, Explosion.WIDTH * explosionSize,
                Explosion.HEIGHT * explosionSize, Explosion.MILLIS_PER_FRAME);

        // Add the explosion and play the sound.
        Explosion explosion = new Explosion(kinematic, getGrid(), animation);
        addEntity(explosion);
        sounds.playSound(Explosion.SOUND_FILENAME);
    }
}
