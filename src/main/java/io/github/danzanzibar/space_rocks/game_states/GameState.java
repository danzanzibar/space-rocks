//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  The abstract base class of all game state classes.
//******************************************************************************
package io.github.danzanzibar.space_rocks.game_states;

import io.github.danzanzibar.space_rocks.SpaceRocksApp;

public abstract class GameState {
    protected final SpaceRocksApp application;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public GameState(SpaceRocksApp application) {
        this.application = application;
    }

    //--------------------------------------------------------------------------
    //  The init method responsible for setting up the game state.
    //--------------------------------------------------------------------------
    public abstract void init();

    //--------------------------------------------------------------------------
    //  The update method responsible for updating each frame.
    //--------------------------------------------------------------------------
    public abstract void update(double dt);

    //--------------------------------------------------------------------------
    //  The input handling method responsible for... handling input.
    //--------------------------------------------------------------------------
    public abstract void handleInput();
}
