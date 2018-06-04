package com.fourmen.actors;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PlayerBounds {
    public final Rectangle collisionRect;

    private float w1, w2,h1,h2;
    //private float y = 78.75f;

    public PlayerBounds(float width, float height){
        w1 = .086f * width;
        w2 = .088f * width;
        h1 = .14f * height;
        h2 = .153333333f * height;
        collisionRect = new Rectangle(w1,h2, width - w1 - w2, height - h1- h2);
    }

    public float getWidth(){
        return collisionRect.width;
    }
    public float getHeight(){
        return collisionRect.height;
    }
    public float getW1(){
        return w1;
    }
    public float getW2(){
        return w2;
    }
    public float getH1(){
        return h1;
    }
    public float getH2(){
        return h2;
    }
    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRect.x,collisionRect.y, collisionRect.width,collisionRect.height);
    }
}
