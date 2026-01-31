//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  An abstract base class for manager classes of various entities.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.space_rocks.spatial.Cell;
import io.github.danzanzibar.space_rocks.spatial.Grid;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class EntityManager {
    private final Grid grid;
    private final List<Entity> entities = new ArrayList<>();

    //--------------------------------------------------------------------------
    //  A constructor for the class that needs a 'Grid'.
    //--------------------------------------------------------------------------
    public EntityManager(Grid grid) {
        this.grid = grid;
    }

    //--------------------------------------------------------------------------
    //  Returns the enclosed 'Grid'.
    //--------------------------------------------------------------------------
    public Grid getGrid() {
        return grid;
    }

    //--------------------------------------------------------------------------
    //  Returns the enclosed list of entities.
    //--------------------------------------------------------------------------
    public List<Entity> getEntities() {
        return entities;
    }

    //--------------------------------------------------------------------------
    //  Updates all the entities.
    //--------------------------------------------------------------------------
    public void update(double dt) {

        // Remove dead entities before updating.
        removeDead();
        for (Entity entity : entities)
            entity.update(dt);
    }

    //--------------------------------------------------------------------------
    //  Draws the entities.
    //--------------------------------------------------------------------------
    public void draw(Graphics2D g, ImageObserver imageObserver) {

        // Don't waste time drawing non-visible entities.
        for (Entity entity : entities) {
            if (entity.getCell().isVisible())
                entity.draw(g, imageObserver);
        }
    }

    //--------------------------------------------------------------------------
    //  Adds an entity.
    //--------------------------------------------------------------------------
    public void addEntity(Entity entity) {
        entities.add(entity);
        grid.updateCell(entity);
    }

    //--------------------------------------------------------------------------
    //  Removes an entity.
    //--------------------------------------------------------------------------
    public void removeDead() {

        // We use this trick to make sure to remove the entity from the cell before returning 'true'.
        entities.removeIf(entity -> {
            if (!entity.isAlive()) {
                Cell cell = entity.getCell();
                if (cell != null)
                    cell.removeEntity(entity);
                return true;
            } else
                return false;
        });
    }

    //--------------------------------------------------------------------------
    //  Clears the list of entities.
    //--------------------------------------------------------------------------
    public void clearEntities() {

        // Must remove entity from cell as part of cleanup.
        for (Entity entity : entities) {
            Cell cell = entity.getCell();

            if (cell != null)
                cell.removeEntity(entity);
        }
        entities.clear();
    }
}
