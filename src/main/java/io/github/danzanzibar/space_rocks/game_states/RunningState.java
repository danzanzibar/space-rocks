//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the game state when the game is running.
//******************************************************************************
package io.github.danzanzibar.space_rocks.game_states;

import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksKeyBindings;
import io.github.danzanzibar.space_rocks.SpaceRocksApp;

public class RunningState extends GameState {
    private int lastScore = 0;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public RunningState(SpaceRocksApp application) {
        super(application);
    }

    //--------------------------------------------------------------------------
    //  The init method.
    //--------------------------------------------------------------------------
    @Override
    public void init() {
        application.getWorld().startGame();
        application.getUi().startGame();
        application.getUi().updateScore(0);
    }

    //--------------------------------------------------------------------------
    //  The update method.
    //--------------------------------------------------------------------------
    @Override
    public void update(double dt) {
        application.getWorld().update(dt);

        // Get the current score and update the UI if it has changed.
        int score = application.getWorld().getScore();
        if (score > lastScore) {
            application.getUi().updateScore(score);
            lastScore = score;
        }

        // If the player has died, go to GameOverState.
        if (!application.getWorld().isPlayerAlive()) {
            GameOverState gameOverState = new GameOverState(application);
            application.setGameState(gameOverState);
            gameOverState.init();
        }
    }

    //--------------------------------------------------------------------------
    //  The input handling method.
    //--------------------------------------------------------------------------
    @Override
    public void handleInput() {
        KeyBindings<SpaceRocksKeyBindings> keyBindings = application.getKeyBindings();

        // Check for pausing.
        if (keyBindings.getKeyState(SpaceRocksKeyBindings.PAUSE)) {
            PausedState pausedState = new PausedState(application);
            application.setGameState(pausedState);
            pausedState.init();
        }
    }
}
