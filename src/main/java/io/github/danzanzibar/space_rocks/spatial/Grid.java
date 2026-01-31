//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a spatial partitioning of user space to allow for
//  improved performance in collision checking and avoidance. 
//******************************************************************************
package io.github.danzanzibar.space_rocks.spatial;

import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.space_rocks.SpaceRocksApp;
import io.github.danzanzibar.space_rocks.SpaceRocksWorld;
import io.github.danzanzibar.space_rocks.entities.Entity;
import io.github.danzanzibar.space_rocks.entities.Player;

import java.awt.*;

public class Grid {

    // This value is added to half the GamePanel width and height for checking if something is potentially visible.
    // Therefore, it should be larger than half the largest width or height of any entity in the game at a minimum.
    // Since we are also using "visible" area only for collision checking, it might as well have a little extra
    // buffer so we don't find explosions appearing at the edges as the player flies into new areas. UFOs will still
    // try to avoid collisions outside visible space but won't be destroyed if they do get too close.
    public static final int MARGIN = 150;

    // Store the user space dimensions.
    private final Dimension userSpaceDims;

    private final int numRows;
    private final int numCols;
    private final Cell[][] cells;

    // Keep track of the visible cells using an index range. This will be the cells we use for drawing and collision
    // checking. We also cache the actual array of visible cells to avoid computing this every frame.
    private int visibleCellStartRow;
    private final int numVisibleRows;
    private int visibleCellStartCol;
    private final int numVisibleCols;
    private final Cell[][] visibleCells;

    // Store the player cell for convenience.
    private Cell playerCell;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Grid(Dimension userSpaceDims) {
        this.userSpaceDims = userSpaceDims;

        // Calculate the total number of cell rows and columns needed for the grid.
        numCols = (userSpaceDims.width + Cell.SIZE - 1) / Cell.SIZE;
        numRows = (userSpaceDims.height + Cell.SIZE - 1) / Cell.SIZE;
        cells = new Cell[numRows][numCols];

        // Initialize the cells.
        for (int row = 0; row < numRows; row++)
            for (int col = 0; col < numCols; col++)
                cells[row][col] = new Cell(row, col);

        // Calculate the displacement from the player's position to the edge of visible space plus some margin.
        int deltaX = SpaceRocksWorld.VISIBLE_WIDTH / 2 + MARGIN;
        int deltaY = SpaceRocksWorld.VISIBLE_HEIGHT / 2 + MARGIN;

        // Cache the total number of visible rows and columns.
        numVisibleCols = 2 * maxCells(deltaX) + 1;
        numVisibleRows = 2 * maxCells(deltaY) + 1;

        // Cache the array of visible cells.
        visibleCells = new Cell[numVisibleRows][numVisibleCols];
    }

    //--------------------------------------------------------------------------
    //  A convenience method to get the player object.
    //--------------------------------------------------------------------------
    public Player getPlayer() {
        return playerCell.getPlayer();
    }

    //--------------------------------------------------------------------------
    //  A convenience method to get the player position.
    //--------------------------------------------------------------------------
    public Vector2 getPlayerPos() {
        return playerCell.getPlayer().getKinematic().getPosition();
    }

    //--------------------------------------------------------------------------
    //  This method returns the 2D array of visible cells.
    //--------------------------------------------------------------------------
    public Cell[][] getVisibleCells() {
        return visibleCells;
    }

    //--------------------------------------------------------------------------
    //  This method returns a toroidal distance measurement between 2 points.
    //--------------------------------------------------------------------------
    public double toroidalDistance(Vector2 pos1, Vector2 pos2) {
        return signedDelta(pos1, pos2).magnitude();
    }

    //--------------------------------------------------------------------------
    //  This method returns the appropriate drawing position for a give position
    //  relative to the player. For instance, if the player were very close to
    //  the left edge and we inputted a pos on the far right edge, this method
    //  would give a position outside of user space to the left of the player.
    //  This would allow it to draw in the correct position relative to the
    //  player.
    //--------------------------------------------------------------------------
    public Vector2 drawCoords(Vector2 pos, Vector2 playerPos) {
        return playerPos.add(signedDelta(pos, playerPos));
    }

    //--------------------------------------------------------------------------
    //  Returns true if this position is visible, false otherwise.
    //--------------------------------------------------------------------------
    public boolean isVisiblePos(Vector2 pos) {
        return whichCell(pos).isVisible();
    }

    //--------------------------------------------------------------------------
    //  This method updates the entity's position in the grid.
    //--------------------------------------------------------------------------
    public void updateCell(Entity entity) {

        // If the entity has not changed cells, early out.
        Cell newCell = whichCell(entity.getKinematic().getPosition());
        Cell oldCell = entity.getCell();
        if (newCell == oldCell)
            return;

        // Remove it from the old cell. The cell may be currently set to null if performing this update for the
        // first time, so don't remove it in that case.
        if (oldCell != null)
            entity.getCell().removeEntity(entity);

        // Add the entity to the new Cell and a reference to the Cell in the entity.
        newCell.addEntity(entity);
        entity.setCell(newCell);

        // If this is the player, update the visible cells reference index.
        if (entity instanceof Player) {
            visibleCellStartCol = addToColIndex(newCell.getCol(), -numVisibleCols / 2);
            visibleCellStartRow = addToRowIndex(newCell.getRow(), -numVisibleRows / 2);
            computeVisibleCells();
        }
    }

    //--------------------------------------------------------------------------
    //  This method returns an array of cells within a given distance of a
    //  specified cell.
    //--------------------------------------------------------------------------
    public Cell[] getCellNeighbourhood(Cell cell, double distance) {
        return getCellNeighbourhood(cell, maxCells(distance));
    }

    //--------------------------------------------------------------------------
    //  This method returns an array of cells within 'cellRadius' cells away
    //  from a specified cell. For instance, if cellRadius were '1', it would
    //  return 9 cells in an array. If it were '2', it would return 25 cells.
    //--------------------------------------------------------------------------
    public Cell[] getCellNeighbourhood(Cell cell, int cellRadius) {
        if (cellRadius < 0)
            throw new IllegalArgumentException("Negative radius not allowed");

        // Calculate the grid length of the neighbourhood.
        int gridLength = 2 * cellRadius + 1;

        // Get the full array sized.
        Cell[] cellNeighbourhood = new Cell[gridLength * gridLength];

        // Determine the starting cells, mindful of the toroidal wrapping.
        int startingRow = addToRowIndex(cell.getRow(), -cellRadius);
        int startingCol = addToColIndex(cell.getCol(), -cellRadius);
        int n = 0;

        // Iterate over the cells and add them to the neighbourhood.
        for (int i = 0; i < gridLength; i++) {
            int row = addToRowIndex(startingRow, i);
            for (int j = 0; j < gridLength; j++) {
                int col = addToColIndex(startingCol, j);
                cellNeighbourhood[n++] = cells[row][col];
            }
        }

        return cellNeighbourhood;
    }

    //--------------------------------------------------------------------------
    //  This method returns a vector that shows the shortest distance (in
    //  toroidal terms) from 'pos2' to 'pos1'.
    //--------------------------------------------------------------------------
    public Vector2 signedDelta(Vector2 pos1, Vector2 pos2) {

        // Get the naive deltas between 2 points.
        double dx = pos1.getX() - pos2.getX();
        double dy = pos1.getY() - pos2.getY();

        double halfWidth = userSpaceDims.width / 2.0;
        double halfHeight = userSpaceDims.height / 2.0;

        // Make sure the deltas are between [-halfWidth, halfWidth).
        if (dx > halfWidth)
            dx -= userSpaceDims.width;
        if (dx < -halfWidth)
            dx += userSpaceDims.width;
        if (dy > halfHeight)
            dy -= userSpaceDims.height;
        if (dy < -halfHeight)
            dy += userSpaceDims.height;

        return new Vector2(dx, dy);
    }

    //--------------------------------------------------------------------------
    //  This method returns a "safe" spawn position - i.e. one that is not
    //  visible.
    //--------------------------------------------------------------------------
    public Vector2 getSafeSpawnPos() {
        Vector2 pos;

        // Create a random point in user space, repeating until you get one outside of visible space.
        do {
            double x = Math.random() * userSpaceDims.width;
            double y = Math.random() * userSpaceDims.height;
            pos = new Vector2(x, y);
        } while (isVisiblePos(pos));

        return pos;
    }

    //--------------------------------------------------------------------------
    //  This method mutates the position to be inside the grid using modulo
    //  math.
    //--------------------------------------------------------------------------
    public void gridMod(Vector2 pos) {
        pos.modulo(userSpaceDims);
    }

    //--------------------------------------------------------------------------
    //  This method re-computes the visible cell array and upates the player
    //  cell.
    //--------------------------------------------------------------------------
    private void computeVisibleCells() {

        // First we have to unset the visible flags on the previous array of cells. If first time, cell will be null.
        for (Cell[] cellRow : visibleCells)
            for (Cell cell : cellRow) {
                if (cell != null)
                    cell.setVisible(false);
            }

        // We iterate over the columns and rows using modulo looping, setting them to visible.
        for (int i = 0; i < numVisibleRows; i++) {
            int row = addToRowIndex(visibleCellStartRow, i);
            for (int j = 0; j < numVisibleCols; j++) {
                int col = addToColIndex(visibleCellStartCol, j);
                cells[row][col].setVisible(true);
                visibleCells[i][j] = cells[row][col];
            }
        }

        // Also store the player cell for easy access.
        int playerRow = addToRowIndex(visibleCellStartRow, numVisibleRows / 2);
        int playerCol = addToColIndex(visibleCellStartCol, numVisibleCols / 2);
        playerCell = cells[playerRow][playerCol];
    }

    //--------------------------------------------------------------------------
    //  This method returns the correct cell to contain a given position.
    //--------------------------------------------------------------------------
    private Cell whichCell(Vector2 pos) {

        // Calculate the correct row and column.
        int col = (int) Math.floor(pos.getX() / Cell.SIZE);
        int row = (int) Math.floor(pos.getY() / Cell.SIZE);

        return cells[row][col];
    }

    //--------------------------------------------------------------------------
    //  This method returns the maximum number of cells a given distance could
    //  span if oriented along either the x or y axes.
    //--------------------------------------------------------------------------
    private int maxCells(double distance) {
        return (int) ((distance + Cell.SIZE - 1) / Cell.SIZE);
    }

    //--------------------------------------------------------------------------
    //  This helper method adds to a column index using modulo math, returning a
    //  result that is still inside the grid array.
    //--------------------------------------------------------------------------
    private int addToColIndex(int col, int n) {
        return Math.floorMod(col + n, numCols);
    }

    //--------------------------------------------------------------------------
    //  This helper method adds to a row index using modulo math, returning a
    //  result that is still inside the grid array.
    //--------------------------------------------------------------------------
    private int addToRowIndex(int row, int n) {
        return Math.floorMod(row + n, numRows);
    }
}
