//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This interface represents an object that can fetch an integer key code.
//******************************************************************************
package io.github.danzanzibar.game_framework.input;

public interface KeyBindable {

    //--------------------------------------------------------------------------
    //  The sole method of the interface - should return an integer key code.
    //--------------------------------------------------------------------------
    int getKeyCode();
}
