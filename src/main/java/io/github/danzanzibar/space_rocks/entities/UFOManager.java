//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This is a manager class for UFOs.
//******************************************************************************
package io.github.danzanzibar.space_rocks.entities;

import io.github.danzanzibar.game_framework.core.ResourceLoader;
import io.github.danzanzibar.game_framework.graphics.Animation;
import io.github.danzanzibar.game_framework.graphics.SpriteSheet;
import io.github.danzanzibar.game_framework.core.Vector2;
import io.github.danzanzibar.game_framework.audio.SoundManager;
import io.github.danzanzibar.space_rocks.spatial.Grid;
import io.github.danzanzibar.steering.Kinematic2D;

import java.awt.image.BufferedImage;

public class UFOManager extends EntityManager {
    public static final int INITIAL_NUM = 5;
    public static final int DEMO_NUM = 30;

    private final SpriteSheet spriteSheet;
    private final SoundManager soundManager;
    private int targetNum;

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------
    public UFOManager(Grid grid, SoundManager soundManager) {
        super(grid);

        // Create and cache the spritesheet for creating new UFOs.
        BufferedImage img = ResourceLoader.loadImage(UFO.IMG_FILENAME);
        spriteSheet = new SpriteSheet(img, UFO.PIXEL_WIDTH, UFO.PIXEL_HEIGHT, UFO.NUM_SPRITES);

        this.soundManager = soundManager;

        targetNum = INITIAL_NUM;
    }

    //--------------------------------------------------------------------------
    //  This sets the target number of UFOs that should exist in game.
    //--------------------------------------------------------------------------
    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum;
    }

    //--------------------------------------------------------------------------
    //  Creates the initial UFOs.
    //--------------------------------------------------------------------------
    public void init() {
        addUfos(targetNum);
    }

    //--------------------------------------------------------------------------
    //  Adds 'n' UFOs to the game.
    //--------------------------------------------------------------------------
    public void addUfos(int n) {
        for (int i = 0; i < n; i++)
            addUfo();
    }

    //--------------------------------------------------------------------------
    //  Add a UFO in non-visible space to the game.
    //--------------------------------------------------------------------------
    private void addUfo() {

        // Get a non-visible spawn location.
        Vector2 pos = getGrid().getSafeSpawnPos();
        Kinematic2D kinematic = new Kinematic2D(pos, new Vector2(), 0.0, 0.0);

        // Create the animation with a random initial frame.
        Animation animation = new Animation(spriteSheet, UFO.WIDTH, UFO.HEIGHT, UFO.MILLIS_PER_FRAME);
        int startingFrame = (int) (Math.random() * animation.getNumSprites());
        animation.setImgIndex(startingFrame);

        UFO ufo = new UFO(kinematic, getGrid(), animation, soundManager);
        addEntity(ufo);
    }

    //--------------------------------------------------------------------------
    //  Updates the UFOs and creates more if needed.
    //--------------------------------------------------------------------------
    public void update(double dt) {
        super.update(dt);

        // Add enough UFOs to get back to target number.
        addUfos(targetNum - getEntities().size());
    }
}
