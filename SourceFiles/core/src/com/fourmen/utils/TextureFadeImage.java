package com.fourmen.utils;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fourmen.tween.SpriteAccessor;
import com.fourmen.tween.TextureOpAccessor;


public class TextureFadeImage extends Image {
    private Texture texture;
    int width, height;
    Float opacity;
    private Sprite sprite;
    private TweenManager tweenManager;

    public TextureFadeImage(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        opacity = new Float(0f);

        tweenManager = new TweenManager();
        Tween.registerAccessor(TextureFadeImage.class, new TextureOpAccessor());
        Tween.setCombinedAttributesLimit(1);
        //blackScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(this, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        //Tween.to(this,SpriteAccessor.ALPHA,.8f).target(0).start(tweenManager);
    }

    public void draw(Batch batch, float parentAlpha) {
        tweenManager.update(1 / 60f);
        //batch.draw(texture, 0,0,width,height);
        sprite = new Sprite(texture);
        sprite.setColor(0, 0, 0, opacity);
        sprite.setSize(width, height);
        //if(opacity<=.06){
        //opacity = 0;
        //}
        //else if(opacity > 0) {
        //opacity -= .06;
        //}
        sprite.draw(batch);
        //System.out.println(opacity + "\n");
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float refreshOpacity) {
        opacity = refreshOpacity;
    }

    public void fadeOut() {
        Tween.to(this, SpriteAccessor.ALPHA, .8f).target(0f).start(tweenManager);
    }

    public void fadeIn() {
        {
            Tween.to(this, SpriteAccessor.ALPHA, 3f).target(1f).start(tweenManager);
        }
    }
}