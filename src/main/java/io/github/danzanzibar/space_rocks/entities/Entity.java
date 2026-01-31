//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This is the base, abstract class of all entities in the game. It has a
//  'Kinematic2D', a 'Drawable', and contains references to the grid and cell it
//  may be in. It also implements the 'Drawable' interface and handles the
//  necessary transforms based on the entity's position and orientation before
//  delegating to the 'Drawable' it contains - typically a Sprite or Animation.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.graphics.Animation;
import io.github.danzanzibar.game_framework.graphics.Drawable;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.space_rocks.spatial.Cell;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public abstract class Entity implements Drawable {
    protected boolean alive;
    protected final Kinematic2D kinematic;
    protected Drawable drawable;
    protected Grid grid;
    protected Cell cell;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Entity(Kinematic2D kinematic, Drawable drawable, Grid grid) {
        this.kinematic = kinematic;
        this.drawable = drawable;
        this.grid = grid;
        alive = true;
        grid.updateCell(this);   // Make sure it is registered with the grid.
    }

    //--------------------------------------------------------------------------
    //  A getter for 'alive'.
    //--------------------------------------------------------------------------
    public boolean isAlive() {
        return alive;
    }

    //--------------------------------------------------------------------------
    //  The default implementation of 'kill' flips the 'alive' flag to off.
    //--------------------------------------------------------------------------
    public void kill() {
        alive = false;
    }

    //--------------------------------------------------------------------------
    //  A getter for 'kinematic'.
    //--------------------------------------------------------------------------
    public Kinematic2D getKinematic() {
        return kinematic;
    }

    //--------------------------------------------------------------------------
    //  Sets the 'Drawable'.
    //--------------------------------------------------------------------------
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    //--------------------------------------------------------------------------
    //  Get the currently occupied 'Cell'.
    //--------------------------------------------------------------------------
    public Cell getCell() {
        return cell;
    }

    //--------------------------------------------------------------------------
    //  Sets the currently occupied 'Cell'.
    //--------------------------------------------------------------------------
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    //--------------------------------------------------------------------------
    //  The implementation of the 'Drawable' interface.
    //--------------------------------------------------------------------------
    public void draw(Graphics2D g, ImageObserver imageObserver) {
        if (drawable != null) {
            AffineTransform oldTransform = g.getTransform();

            // Get the draw position using the grid and the player position. We may draw outside the grid if the
            // player is near the edge.
            Vector2 drawPos = grid.drawCoords(getKinematic().getPosition(), grid.getPlayerPos());

            // Translate by the position and orientation.
            g.translate(drawPos.getX(), drawPos.getY());
            g.rotate(kinematic.getOrientation());

            // Delegate to the drawable to finish drawing.
            drawable.draw(g, imageObserver);
            g.setTransform(oldTransform);
        }
    }
    
    //--------------------------------------------------------------------------
    //  Updates the entity based on elapsed time. Delegates to an abstract
    //  template method, 'updateKinematic'.
    //--------------------------------------------------------------------------
    public void update(double dt) {
        updateKinematic(dt);
        grid.gridMod(kinematic.getPosition());  // Make sure this stays on the grid.
        grid.updateCell(this);            // Update the cells.

        // If the drawable is an animation, we need to allow it to update using 'dt'.
        if (drawable instanceof Animation)
            ((Animation) drawable).update(dt);
    }

    //--------------------------------------------------------------------------
    //  The abstract template method that will define how the object moves.
    //--------------------------------------------------------------------------
    public abstract void updateKinematic(double dt);
}
