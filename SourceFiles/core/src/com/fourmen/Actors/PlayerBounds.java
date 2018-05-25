package com.fourmen.Actors;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PlayerBounds {
    private final Rectangle collisionRect;

    private float x = 45;
    //private float y = 78.75f;

    public PlayerBounds(float width, float height){
        collisionRect = new Rectangle(x,x, width - (x *2), height - (2 * x));
    }

    public float getWidth(){
        return collisionRect.width;
    }
    public float getHeight(){
        return collisionRect.height;
    }
    public float getX(){
        return x;
    }
    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRect.x,collisionRect.y, collisionRect.width,collisionRect.height);
    }
}
