//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a collection of key bindings. It should be supplied
//  with the 'InputMap' and 'ActionMap' from a 'JComponent' along with a enum
//  class object that implements the 'KeyBindable' interface. The enum,
//  therefore, should enumerate all input actions and return key codes for each
//  action.
//******************************************************************************
package io.github.danzanzibar.game_framework.input;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.EnumMap;

public class KeyBindings<K extends Enum<K> & KeyBindable> {
    private final InputMap inputMap;
    private final ActionMap actionMap;

    // Stores the boolean state of each key that is introduced to the bindings.
    private final EnumMap<K, Boolean> map;

    //--------------------------------------------------------------------------
    //  A constructor for the class. 
    //--------------------------------------------------------------------------
    public KeyBindings(InputMap inputMap, ActionMap actionMap, Class<K> bindingsEnumClass) {
        this.inputMap = inputMap;
        this.actionMap = actionMap;
        this.map = new EnumMap<>(bindingsEnumClass);

        // Go through the enums and set the boolean to false initially. Then use 'bindKey' to set up the binding.
        for (K action : bindingsEnumClass.getEnumConstants()) {
            map.put(action, false);
            bindKey(action);
        }
    }

    //--------------------------------------------------------------------------
    //  Get the boolean value representing if the given key is currently pressed.
    //--------------------------------------------------------------------------
    public boolean getKeyState(K action) {
        return map.get(action);
    }

    //--------------------------------------------------------------------------
    //  This method takes a given enumerated action that can also supply a
    //  corresponding key code (and integer value from 'KeyEvent'). It binds
    //  these using Swing's key bindings interface and allows the user to check
    //  the boolean state of the enumerated keys later.
    //--------------------------------------------------------------------------
    private void bindKey(K action) {

        // Generate the strings that will be associated with the actions.
        String onKey = action.name() + "_on";
        String offKey = action.name() + "_off";

        // Get the 'int' keycode from the enum.
        int keyCode = action.getKeyCode();

        // Add the action strings to the input map for the given keycode being pressed and released.
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), onKey);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), offKey);

        // Define the actions to respond to a key pressed and released events.
        Action on = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.put(action, true);
            }
        };
        Action off = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.put(action, false);
            }
        };

        // Add the actions to the action map.
        actionMap.put(onKey, on);
        actionMap.put(offKey, off);
    }
}
