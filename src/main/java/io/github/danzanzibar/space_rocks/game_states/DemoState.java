//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the game state at launch and demos the asteroids and
//  UFOs.
//******************************************************************************
package io.github.danzanzibar.space_rocks.game_states;

import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksKeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksApp;

public class DemoState extends GameState {

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public DemoState(SpaceRocksApp application) {
        super(application);
    }

    //--------------------------------------------------------------------------
    //  The init method.
    //--------------------------------------------------------------------------
    @Override
    public void init() {
        application.getWorld().startDemo();
        application.getUi().showMainMenu();
    }

    //--------------------------------------------------------------------------
    //  The update method.
    //--------------------------------------------------------------------------
    @Override
    public void update(double dt) {
        application.getWorld().update(dt);
    }

    //--------------------------------------------------------------------------
    //  The input handling method.
    //--------------------------------------------------------------------------
    @Override
    public void handleInput() {
        KeyBindings<SpaceRocksKeyBindings> keyBindings = application.getKeyBindings();

        // Check for quitting.
        if (keyBindings.getKeyState(SpaceRocksKeyBindings.QUIT))
            System.exit(0);

        // Check for starting the game.
        if (keyBindings.getKeyState(SpaceRocksKeyBindings.START)) {
            RunningState runningState = new RunningState(application);
            application.setGameState(runningState);
            runningState.init();
        }
    }
}
