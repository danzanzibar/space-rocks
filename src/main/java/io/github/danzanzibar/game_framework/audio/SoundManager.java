//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class is a simple manager for sounds.
//******************************************************************************
package io.github.danzanzibar.game_framework.audio;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    // Stores a map of file names with 'SoundPool's.
    private final Map<String, SoundPool> sounds = new HashMap<>();

    //--------------------------------------------------------------------------
    //  Adds a new 'SoundPool' to the map.
    //--------------------------------------------------------------------------
    public void addSound(String soundFileName, int poolSize) {
        SoundPool sound = new SoundPool(soundFileName, poolSize);
        sounds.put(soundFileName, sound);
    }

    //--------------------------------------------------------------------------
    //  Play a stored 'SoundPool'.
    //--------------------------------------------------------------------------
    public void playSound(String soundFileName) {
        sounds.get(soundFileName).play();
    }
}
