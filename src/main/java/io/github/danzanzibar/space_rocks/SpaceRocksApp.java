//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the overall application. It creates the needed Swing
//  components and game World and wires everything together. It also manages
//  updates and rendering. It implements the 'Updatable' interface to listen to
//  the enclosed 'Loop'.
//******************************************************************************
package io.github.danzanzibar.space_rocks;

import io.github.danzanzibar.game_framework.core.Loop;
import io.github.danzanzibar.game_framework.core.Updatable;
import io.github.danzanzibar.game_framework.graphics.DrawingPanel;
import io.github.danzanzibar.game_framework.input.KeyBindings;
import io.github.danzanzibar.space_rocks.game_states.*;

import javax.swing.*;
import java.awt.*;

public class SpaceRocksApp implements Updatable {
    public static final int TARGET_MILLIS_PER_FRAME = 15;
    public static final Dimension PANEL_SIZE = new Dimension(800, 800);

    private final JLayeredPane mainPane;
    private final KeyBindings<SpaceRocksKeyBindings> keyBindings;
    private final SpaceRocksWorld world;
    private final GameUI ui;

    private GameState gameState;

    //--------------------------------------------------------------------------
    //  A constructor for the class. Does a lot of the heavy lifting.
    //--------------------------------------------------------------------------
    public SpaceRocksApp(JFrame frame) {

        // Make the Loop and have the app listen to it.
        Loop loop = new Loop(TARGET_MILLIS_PER_FRAME);
        loop.attachListener(this);

        // Create the main pane that the UI and drawing panel will be layered onto.
        mainPane = new JLayeredPane();
        mainPane.setPreferredSize(PANEL_SIZE);

        // Create and add the UI.
        ui = new GameUI();
        JPanel uiPanel = ui.getUiPanel();
        uiPanel.setBounds(0, 0, PANEL_SIZE.width, PANEL_SIZE.height);
        mainPane.add(uiPanel, JLayeredPane.PALETTE_LAYER);

        // Create and add the drawing panel.
        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.setBounds(0, 0, PANEL_SIZE.width, PANEL_SIZE.height);
        mainPane.add(drawingPanel, JLayeredPane.DEFAULT_LAYER);

        // Create the keybindings manager.
        InputMap inMap = mainPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = mainPane.getActionMap();
        keyBindings = new KeyBindings<>(inMap, aMap, SpaceRocksKeyBindings.class);

        // Create the game world and attached the drawing panel to it. This will have it render after each update
        // the world does.
        world = new SpaceRocksWorld(keyBindings);
        world.attachDrawingPanel(drawingPanel, world::getView, g -> {
            world.userSpaceTransform(g, frame.getSize());
        });

        // Start the loop and set the initial game state.
        loop.start();
        gameState = new DemoState(this);
        gameState.init();
    }

    //--------------------------------------------------------------------------
    //  Returns the main 'JLayered' pane.
    //--------------------------------------------------------------------------
    public JLayeredPane getMainPane() {
        return mainPane;
    }

    //--------------------------------------------------------------------------
    //  Returns the game world object.
    //--------------------------------------------------------------------------
    public SpaceRocksWorld getWorld() {
        return world;
    }

    //--------------------------------------------------------------------------
    //  Returns the game UI object.
    //--------------------------------------------------------------------------
    public GameUI getUi() {
        return ui;
    }

    //--------------------------------------------------------------------------
    //  Returns the 'KeyBindings' object.
    //--------------------------------------------------------------------------
    public KeyBindings<SpaceRocksKeyBindings> getKeyBindings() {
        return keyBindings;
    }

    //--------------------------------------------------------------------------
    //  Sets the game state.
    //--------------------------------------------------------------------------
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    //--------------------------------------------------------------------------
    //  Updates the application when triggered by the 'Loop' it listens to.
    //  Delegates to the game state objects for what to do.
    //--------------------------------------------------------------------------
    @Override
    public void update(double dt) {

        // Each update, delegate to the game state.
        gameState.update(dt);
        gameState.handleInput();
    }
}
