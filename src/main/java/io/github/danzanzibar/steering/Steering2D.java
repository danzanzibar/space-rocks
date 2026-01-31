//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This record represents the linear and angular accelerations that can be
//  applied to a physics object.
//******************************************************************************
package io.github.danzanzibar.steering;

import io.github.danzanzibar.game_framework.core.Vector2;

public record Steering2D(Vector2 linear, double angular) {

    //--------------------------------------------------------------------------
    //  A default constructor for the class that provides zero valued fields.
    //--------------------------------------------------------------------------
    public Steering2D() {
        this(new Vector2(0, 0), 0);
    }
}
