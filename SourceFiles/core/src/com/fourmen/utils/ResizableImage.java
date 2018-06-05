package com.fourmen.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class ResizableImage  extends Image {
    private Texture texture;
    int width, height;
    public ResizableImage(Texture texture, int width, int height){
        this.texture = texture;
        this.width = width;
        this.height = height;
    }
    public void draw (Batch batch, float parentAlpha){
        batch.draw(texture, 0,0,width,height);
    }
}
