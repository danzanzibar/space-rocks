//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This Interface represents a rendering target.
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public interface Renderer {

    //--------------------------------------------------------------------------
    //  The sole method of the interface responsible for drawing a list of
    //  drawables.
    //--------------------------------------------------------------------------
    void render(List<Drawable> drawables, Consumer<Graphics2D> userSpaceTransform);
}
