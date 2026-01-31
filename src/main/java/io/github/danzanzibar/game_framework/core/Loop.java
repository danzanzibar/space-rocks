//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a game loop that can have listeners attached to it.
//******************************************************************************
package io.github.danzanzibar.game_framework.core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Loop {
    private boolean running;
    private final Timer timer;
    private long lastUpdate;
    private final List<Updatable> listeners = new ArrayList<>();

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Loop(int targetMillisPerFrame) {
        running = false;

        // Create the new Timer and set it to pass along elapsed time to listeners when it fires.
        timer = new Timer(targetMillisPerFrame, e -> {
            long now = System.nanoTime();
            long deltaNano = now - lastUpdate;
            notifyListeners(deltaNano / 1_000_000_000.0);
            lastUpdate = now;
        });
    }

    //--------------------------------------------------------------------------
    //  A getter for 'running'.
    //--------------------------------------------------------------------------
    public boolean isRunning() {
        return running;
    }

    //--------------------------------------------------------------------------
    //  This method attaches an 'Updatable'. All listeners will be notified
    //  when the loop ticks over.
    //--------------------------------------------------------------------------
    public void attachListener(Updatable listener) {
        listeners.add(listener);
    }

    //--------------------------------------------------------------------------
    //  This method removes a listener.
    //--------------------------------------------------------------------------
    public void detachListener(Updatable listener) {
        listeners.remove(listener);
    }

    //--------------------------------------------------------------------------
    //  Starts the loop.
    //--------------------------------------------------------------------------
    public void start() {
        running = true;
        lastUpdate = System.nanoTime();
        timer.start();
    }

    //--------------------------------------------------------------------------
    //  Stops the loop.
    //--------------------------------------------------------------------------
    public void stop() {
        running = false;
        timer.stop();
    }

    //--------------------------------------------------------------------------
    //  A helper method that notifies all listeners.
    //--------------------------------------------------------------------------
    private void notifyListeners(double dt) {
        for (Updatable listener : listeners)
            listener.update(dt);
    }
}
