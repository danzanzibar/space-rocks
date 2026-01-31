//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a single sound clip and has methods for playing it.
//******************************************************************************
package io.github.danzanzibar.game_framework.audio;

import javax.sound.sampled.Clip;

public class Sound {
    private final Clip clip;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public Sound(Clip clip) {
        this.clip = clip;
    }

    //--------------------------------------------------------------------------
    //  Play the sound clip.
    //--------------------------------------------------------------------------
    public void play() {
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    //--------------------------------------------------------------------------
    //  Stop playing the sound clip.
    //--------------------------------------------------------------------------
    public void stop() {
        if (clip.isRunning())
            clip.stop();
    }

    //--------------------------------------------------------------------------
    //  Returns a boolean indicating if the clip is playing.
    //--------------------------------------------------------------------------
    public boolean isPlaying() {
        return clip.isRunning();
    }
}
