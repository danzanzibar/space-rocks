//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This enum class represents the inputs for the game.
//******************************************************************************
package io.github.danzanzibar.space_rocks;

import io.github.danzanzibar.game_framework.input.KeyBindable;

import java.awt.event.KeyEvent;

public enum SpaceRocksKeyBindings implements KeyBindable {

    START(KeyEvent.VK_ENTER),
    QUIT(KeyEvent.VK_Q),
    PAUSE(KeyEvent.VK_ESCAPE),
    THRUST(KeyEvent.VK_UP),
    CCW(KeyEvent.VK_LEFT),
    CW(KeyEvent.VK_RIGHT),
    FIRE(KeyEvent.VK_SPACE);

    private final int keyCode;

    //--------------------------------------------------------------------------
    //  A constructor for the enum class that takes an integer key code.
    //--------------------------------------------------------------------------
    SpaceRocksKeyBindings(int keyCode) {
        this.keyCode = keyCode;
    }

    //--------------------------------------------------------------------------
    //  Returns the key code for the given enum constant.
    //--------------------------------------------------------------------------
    public int getKeyCode() {
        return keyCode;
    }
}
