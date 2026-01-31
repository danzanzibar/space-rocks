//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This interface is designed primarily for Objects that will be drawn using
//  basic Swing operations like 'drawImage' or drawing primitives. The single
//  method is passed typical Swing painting components 'Graphics2D' and an
//  'ImageObserver' that can be needed for drawing images.
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

import java.awt.*;
import java.awt.image.ImageObserver;

public interface Drawable {

    //--------------------------------------------------------------------------
    //  The only method of the interface. Should draw primitives or images.
    //--------------------------------------------------------------------------
    void draw(Graphics2D g, ImageObserver imageObserver);
}
