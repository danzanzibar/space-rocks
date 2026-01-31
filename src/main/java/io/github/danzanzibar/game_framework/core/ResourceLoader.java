//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  A utility class containing static methods for loading images and sounds.
//******************************************************************************
package io.github.danzanzibar.game_framework.core;

import io.github.danzanzibar.game_framework.audio.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {

    //--------------------------------------------------------------------------
    //  This method loads and returns an image.
    //--------------------------------------------------------------------------
    public static BufferedImage loadImage(String imageFile) {
        BufferedImage image;

        try (InputStream in = ResourceLoader.class.getResourceAsStream("/" + imageFile)) {

            // Get the image and check it for null.
            image = ImageIO.read(in);
            if (image == null)
                throw new IllegalArgumentException("Image " + imageFile + " not found");
        }

        // Catch the IOException and provide a little more info.
        catch (IOException e) {
            throw new RuntimeException("Failed to load image " + imageFile, e);
        }

        return image;
    }

    //--------------------------------------------------------------------------
    //  This method loads and returns a 'Sound' object.
    //--------------------------------------------------------------------------
    public static Sound loadSound(String soundFile) {
        try {
            URL url = ResourceLoader.class.getResource("/" + soundFile);

            if (url == null)
                throw new IllegalArgumentException("Sound " + soundFile + " not found");

            try (AudioInputStream audioInputStream =
                         AudioSystem.getAudioInputStream(url)) {

                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                return new Sound(clip);
            }
        }

        // Check the various exceptions.
        catch (UnsupportedAudioFileException e) {
            throw new RuntimeException("Unsupported audio format: " + soundFile, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sound " + soundFile, e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Audio line unavailable for " + soundFile, e);
        }
    }

}
