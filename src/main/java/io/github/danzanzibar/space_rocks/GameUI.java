//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents the game's UI layer. It uses Intellij's Swing Designer
//  and has an associated XML doc.
//******************************************************************************
package io.github.danzanzibar.space_rocks;

import javax.swing.*;
import java.awt.*;

public class GameUI {
    private JPanel uiPanel;
    private JPanel labelPanel;

    private JLabel scoreLabel;
    private JLabel topLabel;
    private JLabel midLabel;
    private JLabel bottomLabel;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public GameUI() {

        // Set transparent.
        uiPanel.setOpaque(false);
        labelPanel.setOpaque(false);

        // Make some different font sizes.
        Font hugeFont = topLabel.getFont().deriveFont(80f);
        Font largeFont = topLabel.getFont().deriveFont(40f);
        Font smallFont = topLabel.getFont().deriveFont(25f);

        // Set up the various labels used for displaying info on the screen.
        topLabel.setFont(hugeFont);
        topLabel.setForeground(Color.RED);

        midLabel.setFont(largeFont);
        midLabel.setForeground(Color.LIGHT_GRAY);

        bottomLabel.setFont(largeFont);
        bottomLabel.setForeground(Color.LIGHT_GRAY);

        scoreLabel.setFont(smallFont);
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setVisible(false);
    }

    //--------------------------------------------------------------------------
    //  Returns the ui JPanel.
    //--------------------------------------------------------------------------
    public JPanel getUiPanel() {
        return uiPanel;
    }

    //--------------------------------------------------------------------------
    //  Shows the main menu at the start of the game launch.
    //--------------------------------------------------------------------------
    public void showMainMenu() {

        // Display the game title.
        topLabel.setText("Space Rocks");

        // Display input instructions.
        midLabel.setText("Press RETURN To Play");
        bottomLabel.setText("Press Q To Quit");

        // Set visible.
        topLabel.setVisible(true);
        midLabel.setVisible(true);
        bottomLabel.setVisible(true);
    }

    //--------------------------------------------------------------------------
    //  Starts the game, hiding most UI elements.
    //--------------------------------------------------------------------------
    public void startGame() {

        // Hide everything.
        topLabel.setVisible(false);
        midLabel.setVisible(false);
        bottomLabel.setVisible(false);

        // Show the score.
        scoreLabel.setVisible(true);
    }

    //--------------------------------------------------------------------------
    //  Updates the score label.
    //--------------------------------------------------------------------------
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + String.format("%,10d", score));
    }

    //--------------------------------------------------------------------------
    //  Show the pause menu.
    //--------------------------------------------------------------------------
    public void showPauseMenu() {
        midLabel.setText("Paused");
        bottomLabel.setText("RETURN To Resume / Q To Quit");
        midLabel.setVisible(true);
        bottomLabel.setVisible(true);
    }

    //--------------------------------------------------------------------------
    //  Resumes the game, hiding most of the UI.
    //--------------------------------------------------------------------------
    public void resume() {
        midLabel.setVisible(false);
        bottomLabel.setVisible(false);
    }

    //--------------------------------------------------------------------------
    //  Shows the game over screen.
    //--------------------------------------------------------------------------
    public void showGameOver(int score) {
        topLabel.setText("Game Over");
        midLabel.setText("Score " + String.format("%,10d", score));
        bottomLabel.setText("Press RETURN To Play / Q To Quit");

        // Set all visible.
        topLabel.setVisible(true);
        midLabel.setVisible(true);
        bottomLabel.setVisible(true);
    }
}
