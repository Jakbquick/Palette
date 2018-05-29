package com.fourmen.Actors;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PlayerBounds {
    public final Rectangle collisionRect;

    private float x, y;
    //private float y = 78.75f;

    public PlayerBounds(float width, float height){
        x = .088f * width;
        y = .14f * height;
        collisionRect = new Rectangle(x,y, width - (x *2), height - (2 * y));
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
    public float getY(){ return y;}
    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRect.x,collisionRect.y, collisionRect.width,collisionRect.height);
    }
}
