//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This abstract class represents a game world object. It provides an interface
//  to attach 'Renderer's to the 'World'. A 'Supplier' functional interface
//  needs to be specified that will provide a "view" of the world, in this case
//  implemented as a list of 'Drawable's. It implements the 'Updatable'
//  interface and tighly couples updating to rendering - this isn't necessarily
//  generally desirable but provides a simple mechanism to connect a world to
//  a renderer for simple games.
//******************************************************************************
package io.github.danzanzibar.game_framework.core;

import io.github.danzanzibar.game_framework.graphics.Drawable;
import io.github.danzanzibar.game_framework.graphics.Renderer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class World implements Updatable {

    // Stores the bindings - see the private record class at the end for more information.
    private final List<RendererViewBinding> bindings = new ArrayList<>();

    //--------------------------------------------------------------------------
    //  Attaches a 'Renderer' to the world. A 'viewSupplier' must be given,
    //  along with a user space transform 'Consumer'.
    //--------------------------------------------------------------------------
    public void attachDrawingPanel(Renderer renderer, Supplier<List<Drawable>> viewSupplier,
                                   Consumer<Graphics2D> userSpaceTransform) {
        bindings.add(new RendererViewBinding(renderer, viewSupplier, userSpaceTransform));
    }

    //--------------------------------------------------------------------------
    //  Remove a given 'Renderer' from the bindings.
    //--------------------------------------------------------------------------
    public void detachDrawingPanel(Renderer renderer) {
        bindings.removeIf(b -> b.renderer == renderer);
    }

    //--------------------------------------------------------------------------
    //  The method from the 'Updatable' interface. It delegates to a template
    //  method 'doUpdate' and then notifies all renderers to draw their views.
    //--------------------------------------------------------------------------
    @Override
    public void update(double dt) {
        doUpdate(dt);

        for (RendererViewBinding binding : bindings) {
            Renderer renderer = binding.renderer;
            Supplier<List<Drawable>> viewSupplier = binding.viewSupplier;

            // Use the 'viewSupplier' to get the list of drawables. Send this back to the renderer with the correct
            // transform.
            List<Drawable> drawables = viewSupplier.get();
            renderer.render(drawables, binding.userSpaceTransform);
        }
    }

    //--------------------------------------------------------------------------
    //  An abstract template method to perform updating in the given world.
    //--------------------------------------------------------------------------
    protected abstract void doUpdate(double dt);

    //--------------------------------------------------------------------------
    //  A private record class to simplify storing the bindings.
    //--------------------------------------------------------------------------
    private record RendererViewBinding(Renderer renderer, Supplier<List<Drawable>> viewSupplier,
                                       Consumer<Graphics2D> userSpaceTransform) { }
}
