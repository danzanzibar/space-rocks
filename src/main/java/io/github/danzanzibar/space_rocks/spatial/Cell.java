//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a single cell of a grid overlaying user space. 
//******************************************************************************
package io.github.danzanzibar.space_rocks.spatial;

import io.github.danzanzibar.space_rocks.entities.*;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    // Not sure what the ideal value for this is, but I think right now I'm using more memory for the sake of
    // performance. This is the length of a square cell.
    public static final int SIZE = 100;

    private final int row;
    private final int col;
    private boolean visible;

    // Store the various entities in a type-aware manner.
    private Player player;
    private final List<Asteroid> asteroids = new ArrayList<>();
    private final List<UFO> ufos = new ArrayList<>();
    private final List<Laser> lasers = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        visible = false;
        player = null;
    }

    //--------------------------------------------------------------------------
    //  A getter for the row.
    //--------------------------------------------------------------------------
    public int getRow() {
        return row;
    }

    //--------------------------------------------------------------------------
    //  A getter for the column.
    //--------------------------------------------------------------------------
    public int getCol() {
        return col;
    }

    //--------------------------------------------------------------------------
    //  A getter for the visible flag.
    //--------------------------------------------------------------------------
    public boolean isVisible() {
        return visible;
    }

    //--------------------------------------------------------------------------
    //  A setter for the visible flag.
    //--------------------------------------------------------------------------
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    //--------------------------------------------------------------------------
    //  Return the player is currently in the cell, otherwise null.
    //--------------------------------------------------------------------------
    public Player getPlayer() {
        return player;
    }

    //--------------------------------------------------------------------------
    //  Returns the list of asteroids in the cell.
    //--------------------------------------------------------------------------
    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    //--------------------------------------------------------------------------
    //  Returns the list of UFOs in the cell.
    //--------------------------------------------------------------------------
    public List<UFO> getUfos() {
        return ufos;
    }

    //--------------------------------------------------------------------------
    //  Returns the list of lasers in the cell.
    //--------------------------------------------------------------------------
    public List<Laser> getLasers() {
        return lasers;
    }

    //--------------------------------------------------------------------------
    //  Returns the list of explosions in the cell.
    //--------------------------------------------------------------------------
    public List<Explosion> getExplosions() {
        return explosions;
    }

    //--------------------------------------------------------------------------
    //  This method adds an entity to the cell.
    //--------------------------------------------------------------------------
    public void addEntity(Entity entity) {

        // We do a rather nasty type checking scheme here, but it provides for much simplification of the collision
        // checking later on... and the intent should be clear: put the entity in the right bucket.
        if (entity instanceof Player)
            player = (Player) entity;
        else if (entity instanceof Asteroid)
            asteroids.add((Asteroid) entity);
        else if (entity instanceof UFO)
            ufos.add((UFO) entity);
        else if (entity instanceof Laser)
            lasers.add((Laser) entity);
        else if (entity instanceof Explosion)
            explosions.add((Explosion)  entity);
    }

    //--------------------------------------------------------------------------
    //  This method removes an entity from the cell.
    //--------------------------------------------------------------------------
    public void removeEntity(Entity entity) {
        if (entity instanceof Player)
            player = null;
        else if (entity instanceof Asteroid)
            asteroids.remove(entity);
        else if (entity instanceof UFO)
            ufos.remove(entity);
        else if (entity instanceof Laser)
            lasers.remove(entity);
        else if (entity instanceof Explosion)
            explosions.remove(entity);
    }
}
