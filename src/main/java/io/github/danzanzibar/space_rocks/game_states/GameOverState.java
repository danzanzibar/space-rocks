//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the game state at Game Over.
//******************************************************************************
package io.github.danzanzibar.space_rocks.game_states;

import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksKeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksApp;

import javax.swing.*;

public class GameOverState extends GameState {

    private boolean inputBlocked;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public GameOverState(SpaceRocksApp application) {
        super(application);
    }

    //--------------------------------------------------------------------------
    //  The init method.
    //--------------------------------------------------------------------------
    @Override
    public void init() {

        // Temporarily block input while we wait for the Game Over screen to show up.
        inputBlocked = true;
        Timer timer = new Timer(1000, e -> {
            inputBlocked = false;
            application.getUi().showGameOver(application.getWorld().getScore());
        });
        timer.setRepeats(false);
        timer.start();
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

        // Check for restarting the game.
        if (keyBindings.getKeyState(SpaceRocksKeyBindings.START) && !inputBlocked) {
            RunningState runningState = new RunningState(application);
            application.setGameState(runningState);
            runningState.init();
        }
    }
}
