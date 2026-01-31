//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class is responsible for launching the application.
//******************************************************************************
package io.github.danzanzibar.space_rocks;

import javax.swing.*;

public class Launcher {

    //--------------------------------------------------------------------------
    //  The 'main' method of the class.
    //--------------------------------------------------------------------------
    public static void main(String[] args) {

        // Start everything on the EDT.
        SwingUtilities.invokeLater(Launcher::createAndShowGUI);
    }

    //--------------------------------------------------------------------------
    //  This method creates the app and window and gets everything going.
    //--------------------------------------------------------------------------
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Space Rocks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the app and set its main pane in the frame.
        SpaceRocksApp app = new SpaceRocksApp(frame);
        frame.setContentPane(app.getMainPane());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
