//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class is a collection of 'Sound's that allows for utilizing multiple
//  instances.
//******************************************************************************
package io.github.danzanzibar.game_framework.audio;

import io.github.danzanzibar.game_framework.core.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

public class SoundPool {
    private final List<Sound> sounds;
    private int index;

    //--------------------------------------------------------------------------
    //  A constructor for the class. Takes a filename and creates a 'poolSize'
    //  pool of 'Sound' instances. 
    //--------------------------------------------------------------------------
    public SoundPool(String soundFileName, int poolSize) {
        sounds = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            sounds.add(ResourceLoader.loadSound(soundFileName));
        }
        index = 0;
    }

    //--------------------------------------------------------------------------
    //  Plays a 'Sound'. It iterates through the pool as this method is called.
    //--------------------------------------------------------------------------
    public void play() {
        sounds.get(index++).play();
        if (index >= sounds.size())
            index = 0;
    }
}
