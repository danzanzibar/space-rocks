//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class is a subclass of 'Animation' that it will only go
//  through the animation one time.
//******************************************************************************
package io.github.danzanzibar.game_framework.graphics;

public class OneShotAnimation extends Animation {

    //--------------------------------------------------------------------------
    //  A constructor for the class.
    //--------------------------------------------------------------------------    
    public OneShotAnimation(SpriteSheet spriteSheet, int spriteWidth, int spriteHeight, int millisBetweenFrame) {
        super(spriteSheet, spriteWidth, spriteHeight, millisBetweenFrame);
    }

    //--------------------------------------------------------------------------
    //  This method overrides the method from 'Animation' such that is only
    //  animates onces through the frames.
    //--------------------------------------------------------------------------
    @Override
    protected void advanceFrame() {
        imgIndex++;

        // Stop the animation after one pass.
        if (imgIndex >= getNumSprites())
            stop();
        else
            sprite.setImg(spriteSheet.getSubImage(imgIndex));
    }
}
