package com.fourmen.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;



public class TextureFadeImage extends Image {
    private Texture texture;
    int width, height;
    int opacity;
    private Sprite sprite;

    public TextureFadeImage(Texture texture, int width, int height){
        this.texture = texture;
        this.width = width;
        this.height = height;
        opacity = 1;
    }
    public void draw (Batch batch, float parentAlpha){
        //batch.draw(texture, 0,0,width,height);
        sprite = new Sprite(texture);
        sprite.setColor(0,0,0,opacity);
        opacity -= .10;
        sprite.draw(batch);
    }
}