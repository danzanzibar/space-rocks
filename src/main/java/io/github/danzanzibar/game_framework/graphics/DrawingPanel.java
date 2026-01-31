//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class is subclass of 'JPanel' that implements the 'Renderer' interface.
//  It is designed to attach to a 'World' object and allow for manual painting.
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DrawingPanel extends JPanel implements Renderer {

    // Store the 'Drawable's to draw.
    private List<Drawable> drawables = new ArrayList<>();

    // A default user space transform is given - i.e. do nothing.
    private Consumer<Graphics2D> userSpaceTransform = g2d -> { };

    //--------------------------------------------------------------------------
    //  This method overrides painting to draw whatever is sent from the
    //  attached 'World'. It sets anti-aliasing and calls the user space
    //  transform function before delegating to each 'Drawable'.
    //--------------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Set anti-aliasing.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Protect the original transform.
        AffineTransform oldTransform = g2d.getTransform();

        // Apply the user space transform. All drawables should be drawing in user space.
        userSpaceTransform.accept(g2d);

        for (Drawable drawable : drawables)
            drawable.draw(g2d, this);

        g2d.setTransform(oldTransform);
    }

    //--------------------------------------------------------------------------
    //  This method take a view from the 'World' (i.e. a list of 'Drawable's)
    //  and a userspace transform. Once it sets them, it calls the EDT to
    //  repaint the component.
    //--------------------------------------------------------------------------
    @Override
    public void render(List<Drawable> drawables, Consumer<Graphics2D> userSpaceTransform) {
        this.drawables = List.copyOf(drawables);  // Should this become multithreaded, this will avoid certain problems.
        this.userSpaceTransform = userSpaceTransform;
        repaint();
    }
}
