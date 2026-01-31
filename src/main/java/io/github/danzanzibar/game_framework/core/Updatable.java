//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This interface represents an object capable of updating based on an elapsed
//  time.
//******************************************************************************
package io.github.danzanzibar.game_framework.core;

public interface Updatable {

    //--------------------------------------------------------------------------
    //  The sole method of the interface. Consumes an elapsed time.
    //--------------------------------------------------------------------------
    void update(double dt);
}
