package com.fourmen.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image
{
    protected Animation<TextureRegion> animation = null;
    private float stateTime = 0;
    private TextureRegion current;
    private int width, height;
    public AnimatedImage(Animation<TextureRegion> animation, int width, int height, char loop) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
        this.width = width;
        this.height = height;


    }

    @Override
    public void act(float delta)
    {
        ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime+=delta, true));
        super.act(delta);
    }

    public void draw (Batch batch, float parentAlpha){
        current = animation.getKeyFrame(stateTime, true);
        batch.draw(current, 0,0,width,height);
    }
    public void setAnimation(Animation<TextureRegion> setAnim){
        animation = setAnim;
    }
}
