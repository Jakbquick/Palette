package com.fourmen.utils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fourmen.tween.FloatAccessor;
import com.fourmen.tween.SpriteAccessor;


public class TextureFadeImage extends Image {
    private Texture texture;
    int width, height;
    float opacity;
    private Sprite sprite;
    private TweenManager tweenManager;

    public TextureFadeImage(Texture texture, int width, int height){
        this.texture = texture;
        this.width = width;
        this.height = height;
        opacity = 1;

        tweenManager = new TweenManager();
        Tween.registerAccessor(Float.class, new FloatAccessor());
        //blackScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(opacity, FloatAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(opacity,FloatAccessor.ALPHA,2).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {

            }
        }).start(tweenManager);
    }
    public void draw (Batch batch, float parentAlpha){
        tweenManager.update(1/60f);
        //batch.draw(texture, 0,0,width,height);
        sprite = new Sprite(texture);
        sprite.setColor(0,0,0,opacity);
        //if(opacity<=.06){
            //opacity = 0;
        //}
        //else if(opacity > 0) {
            //opacity -= .06;
        //}
        sprite.draw(batch);
    }
}