//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the game state when paused.
//******************************************************************************
package io.github.danzanzibar.space_rocks.game_states;

import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksKeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksApp;

public class PausedState extends GameState {

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public PausedState(SpaceRocksApp application) {
        super(application);
    }

    //--------------------------------------------------------------------------
    //  The init method.
    //--------------------------------------------------------------------------
    @Override
    public void init() {
        application.getUi().showPauseMenu();
    }

    //--------------------------------------------------------------------------
    //  The update method.
    //--------------------------------------------------------------------------
    @Override
    public void update(double dt) { }

    //--------------------------------------------------------------------------
    //  The input handling method.
    //--------------------------------------------------------------------------
    @Override
    public void handleInput() {
        KeyBindings<SpaceRocksKeyBindings> keyBindings = application.getKeyBindings();

        // Check for quitting.
        if (keyBindings.getKeyState(SpaceRocksKeyBindings.QUIT))
            System.exit(0);

        // Check for resuming the game. Note we do not init the running state again.
        if (keyBindings.getKeyState(SpaceRocksKeyBindings.START)) {
            application.getUi().resume();
            RunningState runningState = new RunningState(application);
            application.setGameState(runningState);
        }
    }
}
